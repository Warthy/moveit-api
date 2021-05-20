package fr.moveit.api.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import fr.moveit.api.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class JWTProvider {

	private final Logger log = LoggerFactory.getLogger(JWTProvider.class);

	@Value("${security.jwt.base64-secret}")
	private String secretKey;

	@Value("${security.jwt.token-validity-in-seconds}")
	private final long tokenValidityInMilliseconds = 86400;

	@Value("${security.jwt.token-validity-in-seconds-for-remember-me}")
	private final long tokenValidityInMillisecondsForRememberMe = 2592000;

	private Key key;

	private JwtParser parser;

	private static final String AUTHORITIES_KEY = "auth";

	@PostConstruct
	protected void init() {
		key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		parser = Jwts.parserBuilder().setSigningKey(key).build();
	}

	public String createToken(Authentication authentication, Boolean rememberMe) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

		Date now = new Date();
		Date validity = new Date(now.getTime() + (rememberMe ? tokenValidityInMillisecondsForRememberMe : tokenValidityInMilliseconds));
		return Jwts
				.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.setIssuedAt(now)
				.compact();
	}


	public Authentication getAuthentication(String token) {
		Claims claims = parser.parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays
				.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.filter(auth -> !auth.trim().isEmpty())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}


	public boolean validateToken(String token) {
		try {
			parser.parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.info("Invalid JWT token.");
			log.trace("Invalid JWT token trace.", e);
		}
		return false;
	}

}
