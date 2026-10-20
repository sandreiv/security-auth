package com.app.ies.security.auth.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class ColombiaTime {

    public static final ZoneId ZONE = ZoneId.of("America/Bogota");

    private ColombiaTime() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE);
    }
}
