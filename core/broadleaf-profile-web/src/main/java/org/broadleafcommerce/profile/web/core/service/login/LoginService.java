/*-
 * #%L
 * BroadleafCommerce Profile Web
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
package org.broadleafcommerce.profile.web.core.service.login;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.springframework.security.core.Authentication;

public interface LoginService {

    Authentication loginCustomer(Customer customer);

    Authentication loginCustomer(String username, String clearTextPassword);

    /**
     * Establishes an authenticated Broadleaf customer session for a customer that has already been authenticated by
     * an external identity provider (e.g. Google OpenID Connect), without requiring a local password.
     *
     * <p>The customer's authorities are loaded via {@code blUserDetailsService} (which guarantees {@code ROLE_USER}),
     * the {@link org.springframework.security.core.context.SecurityContext} is populated with a normalized,
     * pre-authenticated token, and the standard customer/cart request-state processing is invoked so that downstream
     * commerce state (anonymous merge, cart attach, login events) behaves exactly as it does for form login.</p>
     *
     * @param customer the persisted, provisioned Broadleaf customer to log in
     * @return the resulting {@link Authentication}
     */
    Authentication loginCustomerExternally(Customer customer);

    void logoutCustomer();

}
