package com.app.ies.security.auth.infrastructure.output.persistence.mappers;


import com.app.ies.security.auth.domain.models.UserAuth;
import com.app.ies.security.auth.infrastructure.output.persistence.entities.UserAuthEntity;

public class UserEntityMapper {

    public static UserAuth toUserAuth(UserAuthEntity userAuthEntity) {
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername(userAuthEntity.getUsuario());
        userAuth.setPassword(userAuthEntity.getContrasena());
        userAuth.setId(userAuthEntity.getId());
        userAuth.setRole(userAuthEntity.getTipo());
        userAuth.setName(userAuthEntity.getNombre());
        userAuth.setEnabled(userAuthEntity.getEstado());
        userAuth.setIdPersona(String.valueOf(userAuthEntity.getPegeId()));
        return userAuth;
    }


}
