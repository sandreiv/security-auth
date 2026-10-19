package com.app.ies.security.auth.domain.ports.security;


import com.app.ies.security.auth.domain.models.AplicacionRoles;
import com.app.ies.security.auth.domain.models.RefreshToken;
import com.app.ies.security.auth.domain.models.UserAuth;

import java.util.List;

public interface TokenProviderPort {

    String generateToken(UserAuth user, List<String> roles);

    String generateToken(UserAuth user, List<String> roles,
            List<AplicacionRoles> aplicaciones);

    RefreshToken getRefreshToken(Long userId);

}
