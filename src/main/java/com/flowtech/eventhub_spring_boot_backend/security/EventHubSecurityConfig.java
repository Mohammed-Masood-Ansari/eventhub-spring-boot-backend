package com.flowtech.eventhub_spring_boot_backend.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class EventHubSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			/* SecurityContextRepository contextRepository */ JwtAuthenticationConverter authenticationConverter)
			throws Exception {
		http.csrf(csrf -> csrf.disable())

				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				/*
				 * .securityContext(security->security.securityContextRepository(
				 * contextRepository))
				 */

				.authorizeHttpRequests(

						a -> a.requestMatchers("/auth/**").permitAll()
								.requestMatchers("/admin/**").hasRole("ADMIN")
								.requestMatchers("/organiser/**").hasRole("ORGANISER")
								.requestMatchers("/customer/**").hasRole("CUSTOMER")
								.anyRequest().authenticated())

				.httpBasic(Customizer.withDefaults())
				.logout(logout -> logout.logoutUrl("/auth/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
						.logoutSuccessUrl("/auth/login"))
				.oauth2ResourceServer(
						server -> server.jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter)))
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {

		return configuration.getAuthenticationManager();
	}

	/*
	 * //this is for session
	 * 
	 * @Bean public SecurityContextRepository contextRepository() {
	 * 
	 * return new HttpSessionSecurityContextRepository(); }
	 */

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public KeyPair keyPair() throws NoSuchAlgorithmException {

		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

		generator.initialize(2048);

		return generator.generateKeyPair();
	}

	@Bean
	public JwtEncoder jwtEncoder(KeyPair keyPair) {

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		return NimbusJwtEncoder.withKeyPair(publicKey, privateKey).build();
	}

	@Bean
	public JwtDecoder jwtDecoder(KeyPair keyPair) {

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {

		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

		grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}
}
