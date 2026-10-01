package com.flowtech.eventhub_spring_boot_backend.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventJwtCodeGenerator {

	private final JwtEncoder jwtEncoder;

	public String generateJwtCode(Authentication authentication) {

		Instant instantNow = Instant.now();

		List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

		// 1. Define the JWT claims
		JwtClaimsSet claims = JwtClaimsSet.builder().issuer("event-hub").subject(authentication.getName()) // Or
																											// authenticator.getName()
				.issuedAt(instantNow).expiresAt(instantNow.plus(1, ChronoUnit.HOURS)) // Set expiration time
				.claim("scope", roles) // Custom claims if needed
				.build();
		// 2. Encode and return the token string
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}
}
