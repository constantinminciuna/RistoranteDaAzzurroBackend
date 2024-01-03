package it.cab.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.cab.security.config.JwtService;
import it.cab.security.token.Token;
import it.cab.security.token.TokenRepository;
import it.cab.security.token.TokenType;
import it.cab.security.user.Role;
import it.cab.security.user.User;
import it.cab.security.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;
	
	public AuthenticationResponse register(RegisterRequest request) {
		var user = User.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(Role.ROLE_CLIENTE)
				.build();
		var savedUser = userRepository.save(user);
		var jwtToken = jwtService.generateToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
	
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), 
						request.getPassword()
				)
		);
		var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.username(user.getUsername())
				.role(user.getRole().toString())
				.idUtente(user.getId())
				.build();
	}
	
	private void revokeAllUserTokens(User user) {
		var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
		if(validUserTokens.isEmpty()) {
			return;
		}
		validUserTokens.forEach(t -> {
			t.setExpired(true);
			t.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
	
	private void saveUserToken(User savedUser, String jwtToken) {
		var token = Token.builder()
				.user(savedUser)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepository.save(token);
	}
}
