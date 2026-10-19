-- Índice único del token público. Ejecutar una vez en des_privada.
CREATE UNIQUE INDEX IF NOT EXISTS uk_refreshtoken_tokenid
    ON vortal.refreshtoken (reto_tokenid);
