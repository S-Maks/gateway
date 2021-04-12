package com.msp.invoice.gateway;

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @GetMapping({"/", "/sso/login"})
    public String ssoLogin(@AuthenticationPrincipal(expression = "keycloakSecurityContext")
                                   RefreshableKeycloakSecurityContext principal) {
        return principal.getTokenString();
    }
}
