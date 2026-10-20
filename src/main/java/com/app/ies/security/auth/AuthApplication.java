package com.app.ies.security.auth;

import com.app.ies.security.auth.domain.ColombiaTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ColombiaTime.ZONE));
		SpringApplication.run(AuthApplication.class, args);
	}

}
