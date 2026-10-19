package com.app.ies.security.auth.infrastructure.security.adapters;

import com.app.ies.security.auth.infrastructure.security.adapters.legacysecurity.Algorithm;
import org.springframework.stereotype.Component;


@Component
public class LegacyPbeValidatorAdapter implements ILegacyPbeValidatorAdapter {

    private final String secretKeyLegacy = "Vortal Hermesoft - Plataforma - Universidad de Pamplona";

    @Override
    public boolean verify(String rawPassword, String hexEncodedPassword, String idUsuario, String idPersona) {
        try {
                String pwdBD = hexEncodedPassword;
                String pwdForm = rawPassword;
                long id = Long.parseLong(idUsuario);
                int it = (int)(id % 5L);
                String strPwd = idUsuario + pwdForm + idPersona;
                String md5 = Algorithm.getStringMessageDigest(strPwd, Algorithm.MD5, it);
                System.out.println("::::::::::::::>md5: " + md5);
                System.out.println("::::::::::::::>pwdBD: " + pwdBD);
                if (!pwdBD.equals(md5)) {
                    String msg = "4. Usuario [ " + "voForm.getUsuario()" + " ], la contrasecon la que desea ingresar es incorrecta.";
                    System.out.println(msg);
                }else{
                    return true;
                }

        } catch (Exception e) {
            System.err.println("Fallo usando el combo de JARs legacy: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return false;
    }

}