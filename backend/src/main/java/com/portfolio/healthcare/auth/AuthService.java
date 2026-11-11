package com.portfolio.healthcare.auth;

import com.portfolio.healthcare.security.AppUser;
import com.portfolio.healthcare.security.AppUserRepository;
import com.portfolio.healthcare.shared.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.JwsHeader;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Locale;

@Service
public class AuthService {
    private final AppUserRepository users;
    private final PasswordEncoder passwords;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final long tokenTtlMinutes;

    public AuthService(AppUserRepository users, PasswordEncoder passwords,
                       AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                       @Value("${app.jwt.ttl-minutes:60}") long tokenTtlMinutes) {
        this.users = users; this.passwords = passwords; this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder; this.tokenTtlMinutes = tokenTtlMinutes;
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        String email = normalize(request.email());
        if (users.existsByEmailIgnoreCase(email)) throw new DuplicateResourceException("Email already exists");
        users.save(new AppUser(email, passwords.encode(request.password()), "PATIENT"));
        return issueToken(email, "PATIENT");
    }

    public AuthResponse login(AuthRequest request) {
        String email = normalize(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        AppUser user = users.findByEmailIgnoreCase(email).orElseThrow();
        return issueToken(email, user.getRole());
    }

    private AuthResponse issueToken(String email, String role) {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(tokenTtlMinutes * 60);
        JwtClaimsSet claims = JwtClaimsSet.builder().issuer("healthcare-management-platform")
            .issuedAt(now).expiresAt(expires).subject(email).claim("role", role).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new AuthResponse(token, "Bearer", expires);
    }

    private String normalize(String email) { return email.trim().toLowerCase(Locale.ROOT); }
}