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

import org.apache.commons.lang3.StringUtils;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * Default implementation of {@link CustomerOAuth2ProvisioningService}.
 *
 * <p>Resolves a Broadleaf {@link Customer} from a federated identity, provisioning a new registered customer on
 * first login and matching returning customers by the provider subject stored on
 * {@link Customer#getExternalId()}. See GitHub issue #28.</p>
 *
 * @author BroadleafCommerce (issue #28)
 */
@Service("blCustomerOAuth2ProvisioningService")
public class CustomerOAuth2ProvisioningServiceImpl implements CustomerOAuth2ProvisioningService {

    @Resource(name = "blCustomerService")
    protected CustomerService customerService;

    @Override
    public Customer provisionCustomer(String provider, String subject, String email, boolean emailVerified,
                                      String firstName, String lastName) {
        if (StringUtils.isBlank(subject)) {
            throw new IllegalArgumentException("A non-blank provider subject is required for external authentication");
        }
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("A non-blank email is required for external authentication");
        }
        // Email is only trusted as a discovery/linking signal once the provider asserts it is verified.
        if (!emailVerified) {
            throw new IllegalArgumentException("The provider must assert a verified email before it can be trusted");
        }

        // Returning user: match on the durable federated key and synchronize profile attributes.
        Customer existing = customerService.readCustomerByExternalId(subject);
        if (existing != null) {
            syncProfile(existing, firstName, lastName, email);
            return customerService.saveCustomer(existing);
        }

        // Account-takeover protection: a verified email that resolves to a pre-existing account which is not yet
        // linked to this external identity must not be silently taken over. Defer to an explicit linking flow.
        Customer emailMatch = customerService.readCustomerByEmail(email);
        if (emailMatch != null) {
            throw new ExistingLocalAccountException(
                    "A customer already exists for email '" + email + "' that is not linked to " + provider
                            + "; explicit account linking is required");
        }

        // First-time user: create a new registered customer linked by the external id, with ROLE_USER.
        return createLinkedCustomer(subject, email, firstName, lastName);
    }

    protected Customer createLinkedCustomer(String subject, String email, String firstName, String lastName) {
        Customer customer = customerService.createCustomerWithNullId();
        // Storefront login is username-based; the email doubles as the username (use.email.for.site.login default).
        customer.setUsername(email);
        customer.setExternalId(subject);
        customer.setRegistered(true);
        // Intentionally no local password is set: this is an OAuth-only account and must not permit form login
        // until the customer explicitly establishes a local credential.
        syncProfile(customer, firstName, lastName, email);

        customer = customerService.saveCustomer(customer);
        customerService.createRegisteredCustomerRoles(customer);
        return customer;
    }

    protected void syncProfile(Customer customer, String firstName, String lastName, String email) {
        if (StringUtils.isNotBlank(firstName)) {
            customer.setFirstName(firstName);
        }
        if (StringUtils.isNotBlank(lastName)) {
            customer.setLastName(lastName);
        }
        if (StringUtils.isNotBlank(email)) {
            customer.setEmailAddress(email);
        }
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }
}
