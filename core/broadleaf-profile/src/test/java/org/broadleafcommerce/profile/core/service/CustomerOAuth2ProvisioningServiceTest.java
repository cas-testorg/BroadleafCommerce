/*-
 * #%L
 * BroadleafCommerce Profile
 * %%
 * Copyright (C) 2009 - 2026 Broadleaf Commerce
 * %%
 * Licensed under the Broadleaf Fair Use License Agreement, Version 1.0
 * (the "Fair Use License" located  at http://license.broadleafcommerce.org/fair_use_license-1.0.txt)
 * unless the restrictions on use therein are violated and require payment to Broadleaf in which case
 * the Broadleaf End User License Agreement (EULA), Version 1.1
 * (the "Commercial License" located at http://license.broadleafcommerce.org/commercial_license-1.1.txt)
 * shall apply.
 * 
 * Alternatively, the Commercial License may be replaced with a mutually agreed upon license (the "Custom License")
 * between you and Broadleaf Commerce. You may not use this file except in compliance with the applicable license.
 * #L%
 */
package org.broadleafcommerce.profile.core.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link CustomerOAuth2ProvisioningService}, written before the implementation (TDD) to specify the
 * customer provisioning/linking behavior required by GitHub issue #28 ("Sign in with Google").
 *
 * <p>These tests assert the security-critical invariants: durable linking by provider subject
 * ({@link Customer#getExternalId()}), verified-email enforcement, and no silent takeover of a pre-existing local
 * account via email matching.</p>
 *
 * @author BroadleafCommerce (issue #28)
 */
public class CustomerOAuth2ProvisioningServiceTest {

    private static final String PROVIDER = "google";
    private static final String SUBJECT = "google-sub-123";
    private static final String EMAIL = "jane@example.com";

    private CustomerService customerService;
    private CustomerOAuth2ProvisioningServiceImpl provisioningService;

    @Before
    public void setUp() {
        customerService = createMock(CustomerService.class);
        provisioningService = new CustomerOAuth2ProvisioningServiceImpl();
        provisioningService.setCustomerService(customerService);
    }

    /**
     * AC3: first-time Google users automatically create a customer that is registered, carries ROLE_USER, and is
     * linked by the provider subject.
     */
    @Test
    public void firstTimeGoogleUserCreatesRegisteredCustomerLinkedByExternalId() {
        CustomerImpl fresh = new CustomerImpl();

        expect(customerService.readCustomerByExternalId(SUBJECT)).andReturn(null);
        expect(customerService.readCustomerByEmail(EMAIL)).andReturn(null);
        expect(customerService.createCustomerWithNullId()).andReturn(fresh);
        expect(customerService.saveCustomer(fresh)).andReturn(fresh);
        customerService.createRegisteredCustomerRoles(fresh);
        expectLastCall();
        replay(customerService);

        Customer result = provisioningService.provisionCustomer(PROVIDER, SUBJECT, EMAIL, true, "Jane", "Doe");

        assertNotNull(result);
        assertEquals(SUBJECT, result.getExternalId());
        assertEquals(EMAIL, result.getEmailAddress());
        assertEquals(EMAIL, result.getUsername());
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue("A provisioned OAuth customer must be registered", result.isRegistered());
        verify(customerService);
    }

    /**
     * AC4: returning Google users are matched to their existing customer record via the external id, without
     * creating a new account or re-provisioning roles.
     */
    @Test
    public void returningGoogleUserMatchedByExternalId() {
        CustomerImpl existing = new CustomerImpl();
        existing.setId(42L);
        existing.setExternalId(SUBJECT);
        existing.setUsername(EMAIL);
        existing.setEmailAddress(EMAIL);
        existing.setRegistered(true);

        expect(customerService.readCustomerByExternalId(SUBJECT)).andReturn(existing);
        // AC5: profile information is synchronized from Google on return.
        expect(customerService.saveCustomer(existing)).andReturn(existing);
        replay(customerService);

        Customer result = provisioningService.provisionCustomer(PROVIDER, SUBJECT, EMAIL, true, "Jane", "Doe");

        assertSame("Returning users must resolve to their existing record", existing, result);
        assertEquals(Long.valueOf(42L), result.getId());
        // createCustomerWithNullId / createRegisteredCustomerRoles must NOT be called (enforced by strict mock).
        verify(customerService);
    }

    /**
     * Security: an unverified provider email must be rejected before any lookup or account creation occurs.
     */
    @Test
    public void unverifiedEmailIsRejected() {
        replay(customerService);

        try {
            provisioningService.provisionCustomer(PROVIDER, SUBJECT, EMAIL, false, "Jane", "Doe");
            fail("Expected IllegalArgumentException for an unverified email");
        } catch (IllegalArgumentException expected) {
            // expected
        }

        verify(customerService);
    }

    /**
     * Security (AC2/account-takeover): when the verified email resolves to a pre-existing local (password-based)
     * account that is not yet linked to the external identity, the service must NOT silently link/return it.
     */
    @Test
    public void emailCollisionWithLocalAccountIsNotSilentlyLinked() {
        CustomerImpl localAccount = new CustomerImpl();
        localAccount.setId(7L);
        localAccount.setUsername(EMAIL);
        localAccount.setEmailAddress(EMAIL);
        localAccount.setPassword("$2a$10$existinglocalpasswordhashxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

        expect(customerService.readCustomerByExternalId(SUBJECT)).andReturn(null);
        expect(customerService.readCustomerByEmail(EMAIL)).andReturn(localAccount);
        replay(customerService);

        try {
            provisioningService.provisionCustomer(PROVIDER, SUBJECT, EMAIL, true, "Jane", "Doe");
            fail("Expected ExistingLocalAccountException to prevent silent account takeover");
        } catch (CustomerOAuth2ProvisioningService.ExistingLocalAccountException expected) {
            // expected
        }

        assertNull("A colliding local account must not be linked to the external id", localAccount.getExternalId());
        verify(customerService);
    }
}
