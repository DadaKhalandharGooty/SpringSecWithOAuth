package com.example.demo.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JWTService;
import com.example.demo.user.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public AuthenticationResponse register(RegisterRequest registerRequest) {
		User user = new User();
		user.setFirstName(registerRequest.getFirstName());
		user.setLastName(registerRequest.getLastName());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		user.setRole(Role.USER);
		userRepository.save(user);
		
		String jwtToken = jwtService.generateToken(user);
		AuthenticationResponse authReponse = new AuthenticationResponse();
		authReponse.setToken(jwtToken);
		return authReponse;
	}

	public AuthenticationResponse authenticate(AuthenticationRequest registerRequest) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						registerRequest.getEmail(),
						registerRequest.getPassword()
			));
		
		Optional<User> user = userRepository.findById(registerRequest.getEmail());
		String jwtToken = jwtService.generateToken(user.get());
		AuthenticationResponse authReponse = new AuthenticationResponse();
		authReponse.setToken(jwtToken);
		return authReponse;
	}

	
}
