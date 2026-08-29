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

import org.broadleafcommerce.profile.core.domain.Customer;

/**
 * Resolves a Broadleaf {@link Customer} from an external (federated) identity such as a Google OpenID Connect
 * login, provisioning a new customer on first login and matching returning customers to their existing record.
 *
 * <p>This is the storefront analogue of the admin-side provisioning pattern
 * ({@code AdminUserProvisioningService}). It intentionally converges federated authentication back onto the
 * canonical Broadleaf {@link Customer} so that downstream authorization ({@code ROLE_USER}) and the
 * customer/cart request-state lifecycle continue to function unchanged.</p>
 *
 * <p>Identity-linking invariants (see GitHub issue #28):</p>
 * <ul>
 *     <li>The provider {@code subject} (e.g. Google {@code sub}) is the durable federated key, persisted on
 *     {@link Customer#getExternalId()}.</li>
 *     <li>Email is only a discovery signal and must be verified by the provider before it is trusted.</li>
 *     <li>A federated login must never be silently attached to a pre-existing local (password-based) account
 *     purely because the email addresses match (account-takeover protection).</li>
 * </ul>
 *
 * @author BroadleafCommerce (issue #28)
 */
public interface CustomerOAuth2ProvisioningService {

    /**
     * Looks up-or-creates the {@link Customer} associated with the supplied federated identity.
     *
     * <ul>
     *     <li>If a customer already exists with a matching {@code subject} on {@link Customer#getExternalId()},
     *     that customer is returned (with profile attributes synchronized where appropriate).</li>
     *     <li>If no such customer exists and no conflicting local account exists, a new registered customer is
     *     created with {@code ROLE_USER} and the external id populated.</li>
     * </ul>
     *
     * @param provider      the external provider registration id (e.g. {@code "google"})
     * @param subject       the stable, provider-issued unique subject identifier (e.g. Google {@code sub})
     * @param email         the email address asserted by the provider
     * @param emailVerified whether the provider asserts the email address has been verified
     * @param firstName     the given name asserted by the provider (may be {@code null})
     * @param lastName      the family name asserted by the provider (may be {@code null})
     * @return the persisted, linked Broadleaf {@link Customer}
     * @throws IllegalArgumentException      if {@code subject} is blank, or {@code email} is required but unverified
     * @throws ExistingLocalAccountException if a local (password-based) account already exists for the email and
     *                                       is not yet linked to this external identity
     */
    Customer provisionCustomer(String provider, String subject, String email, boolean emailVerified,
                               String firstName, String lastName);

    /**
     * Thrown when a federated login resolves (by email) to a pre-existing local account that is not linked to the
     * external identity. Callers should route the user through an explicit "verify local password to link" flow
     * rather than silently taking over the account.
     */
    class ExistingLocalAccountException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ExistingLocalAccountException(String message) {
            super(message);
        }
    }
}
