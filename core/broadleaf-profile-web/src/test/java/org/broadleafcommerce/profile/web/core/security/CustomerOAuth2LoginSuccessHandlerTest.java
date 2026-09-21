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

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.core.domain.CustomerImpl;
import org.broadleafcommerce.profile.core.service.CustomerOAuth2ProvisioningService;
import org.broadleafcommerce.profile.web.core.service.login.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Unit tests for {@link CustomerOAuth2LoginSuccessHandler}, written before the implementation (TDD) to specify how
 * a Google (OIDC) login is normalized into a Broadleaf customer session (GitHub issue #28).
 *
 * @author BroadleafCommerce (issue #28)
 */
public class CustomerOAuth2LoginSuccessHandlerTest {

    private CustomerOAuth2ProvisioningService provisioningService;
    private LoginService loginService;
    private AuthenticationSuccessHandler delegate;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private CustomerOAuth2LoginSuccessHandler handler;

    @Before
    public void setUp() {
        provisioningService = createMock(CustomerOAuth2ProvisioningService.class);
        loginService = createMock(LoginService.class);
        delegate = createMock(AuthenticationSuccessHandler.class);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);

        handler = new CustomerOAuth2LoginSuccessHandler(provisioningService, loginService);
        handler.setDelegate(delegate);
    }

    /**
     * A successful Google login maps OIDC claims (sub/email/email_verified/given_name/family_name), provisions/links
     * the customer, establishes the Broadleaf session via the password-less external login, then delegates redirect.
     */
    @Test
    public void googleLoginProvisionsCustomerAndEstablishesSession() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "google-sub-123");
        attributes.put("email", "jane@example.com");
        attributes.put("email_verified", Boolean.TRUE);
        attributes.put("given_name", "Jane");
        attributes.put("family_name", "Doe");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        DefaultOAuth2User principal = new DefaultOAuth2User(authorities, attributes, "sub");
        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(principal, authorities, "google");

        Customer customer = new CustomerImpl();
        customer.setUsername("jane@example.com");
        customer.setExternalId("google-sub-123");

        expect(provisioningService.provisionCustomer("google", "google-sub-123", "jane@example.com", true, "Jane", "Doe"))
                .andReturn(customer);
        expect(loginService.loginCustomerExternally(customer)).andReturn(null);
        delegate.onAuthenticationSuccess(eq(request), eq(response), anyObject());
        expectLastCall();

        replay(provisioningService, loginService, delegate, request, response);

        handler.onAuthenticationSuccess(request, response, oauthToken);

        verify(provisioningService, loginService, delegate, request, response);
    }

    /**
     * A string-typed {@code email_verified} claim (some providers serialize it as a string) is still honored.
     */
    @Test
    public void stringEmailVerifiedClaimIsHonored() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "google-sub-999");
        attributes.put("email", "sam@example.com");
        attributes.put("email_verified", "true");
        attributes.put("given_name", "Sam");
        attributes.put("family_name", "Rivers");

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        DefaultOAuth2User principal = new DefaultOAuth2User(authorities, attributes, "sub");
        OAuth2AuthenticationToken oauthToken = new OAuth2AuthenticationToken(principal, authorities, "google");

        Customer customer = new CustomerImpl();
        customer.setUsername("sam@example.com");

        expect(provisioningService.provisionCustomer("google", "google-sub-999", "sam@example.com", true, "Sam", "Rivers"))
                .andReturn(customer);
        expect(loginService.loginCustomerExternally(customer)).andReturn(null);
        delegate.onAuthenticationSuccess(eq(request), eq(response), anyObject());
        expectLastCall();

        replay(provisioningService, loginService, delegate, request, response);

        handler.onAuthenticationSuccess(request, response, oauthToken);

        verify(provisioningService, loginService, delegate, request, response);
    }
}
