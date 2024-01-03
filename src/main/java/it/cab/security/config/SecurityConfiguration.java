package it.cab.security.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final LogoutService logoutService;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(csrf -> csrf
					.disable()
			)
			.cors(cors -> cors
					.configurationSource(corsConfigurationSource())
			)
			.authorizeHttpRequests(req -> req
					.requestMatchers("/api/menu/admin/**")
					.hasRole("RISTORATORE")
					.requestMatchers("/api/ordine/accetta")
					.hasRole("RISTORATORE")
					.requestMatchers("/api/ordine/**")
					.authenticated()
					.anyRequest()
					.permitAll()
			)
			.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			.logout(logout -> logout
					.logoutUrl("/api/auth/logout")
					.addLogoutHandler(logoutService)
					.logoutSuccessHandler((request, response, authentication) -> 
							SecurityContextHolder.clearContext())
			);
			
			
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowCredentials(true); // This is important since we are using session cookies
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
}
