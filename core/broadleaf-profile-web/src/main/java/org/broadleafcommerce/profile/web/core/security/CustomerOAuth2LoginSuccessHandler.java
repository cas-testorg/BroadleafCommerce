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
package org.broadleafcommerce.profile.web.core.security;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.service.CustomerOAuth2ProvisioningService;
import org.broadleafcommerce.profile.web.core.service.login.LoginService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Spring Security {@link AuthenticationSuccessHandler} for storefront OAuth2/OIDC ("Sign in with Google") logins.
 *
 * <p>After Spring Security has authenticated the user against the external provider (producing an
 * {@link OAuth2AuthenticationToken}), this handler maps the provider claims onto a Broadleaf {@link Customer} via
 * {@link CustomerOAuth2ProvisioningService} (creating/linking as needed), then normalizes the authentication into a
 * standard, password-less Broadleaf customer session via {@link LoginService#loginCustomerExternally(Customer)} so
 * that downstream authorization ({@code ROLE_USER}) and customer/cart request-state processing behave exactly as
 * they do for form login. Redirect handling is delegated to a configurable success handler.</p>
 *
 * <p>Claim attribute names default to the Google/OIDC standard set and can be overridden for other providers.</p>
 *
 * @author BroadleafCommerce (issue #28)
 */
public class CustomerOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    protected final CustomerOAuth2ProvisioningService provisioningService;
    protected final LoginService loginService;

    protected AuthenticationSuccessHandler delegate = new SavedRequestAwareAuthenticationSuccessHandler();

    protected String subjectAttribute = "sub";
    protected String emailAttribute = "email";
    protected String emailVerifiedAttribute = "email_verified";
    protected String firstNameAttribute = "given_name";
    protected String lastNameAttribute = "family_name";

    public CustomerOAuth2LoginSuccessHandler(CustomerOAuth2ProvisioningService provisioningService,
                                             LoginService loginService) {
        this.provisioningService = provisioningService;
        this.loginService = loginService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Authentication established = authentication;
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User principal = oauthToken.getPrincipal();
            String provider = oauthToken.getAuthorizedClientRegistrationId();

            Customer customer = provisioningService.provisionCustomer(
                    provider,
                    principal.getAttribute(subjectAttribute),
                    principal.getAttribute(emailAttribute),
                    isClaimTrue(principal.getAttribute(emailVerifiedAttribute)),
                    principal.getAttribute(firstNameAttribute),
                    principal.getAttribute(lastNameAttribute)
            );

            Authentication customerAuthentication = loginService.loginCustomerExternally(customer);
            if (customerAuthentication != null) {
                established = customerAuthentication;
            }
        }
        delegate.onAuthenticationSuccess(request, response, established);
    }

    /**
     * Coerces an OIDC {@code email_verified} claim, which may be serialized as a {@link Boolean} or a {@link String},
     * into a boolean.
     */
    protected boolean isClaimTrue(Object claim) {
        if (claim instanceof Boolean booleanClaim) {
            return booleanClaim;
        }
        if (claim instanceof String stringClaim) {
            return Boolean.parseBoolean(stringClaim);
        }
        return false;
    }

    public void setDelegate(AuthenticationSuccessHandler delegate) {
        this.delegate = delegate;
    }

    public void setSubjectAttribute(String subjectAttribute) {
        this.subjectAttribute = subjectAttribute;
    }

    public void setEmailAttribute(String emailAttribute) {
        this.emailAttribute = emailAttribute;
    }

    public void setEmailVerifiedAttribute(String emailVerifiedAttribute) {
        this.emailVerifiedAttribute = emailVerifiedAttribute;
    }

    public void setFirstNameAttribute(String firstNameAttribute) {
        this.firstNameAttribute = firstNameAttribute;
    }

    public void setLastNameAttribute(String lastNameAttribute) {
        this.lastNameAttribute = lastNameAttribute;
    }
}
