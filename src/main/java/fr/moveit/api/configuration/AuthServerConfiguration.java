package fr.moveit.api.configuration;

import fr.moveit.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


@Configuration
@EnableAuthorizationServer
public class AuthServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${jwt.secret}")
	private String jwtSecret;

	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	@Autowired
	public AuthServerConfiguration(AuthenticationManager authenticationManager, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("moveit")
				.secret("{noop}secret")
				.authorizedGrantTypes("password", "refresh_token")
				.scopes("moveit")
				.accessTokenValiditySeconds(24 * 60 * 30) 			// 12 hours
				.refreshTokenValiditySeconds(24 * 60 * 30 * 12); 	// 6 days
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.tokenStore(tokenStore())
				.authenticationManager(authenticationManager)
				.userDetailsService(userService)
				.accessTokenConverter(tokenConverter());

	}

	@Bean
	public JwtAccessTokenConverter tokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(this.jwtSecret);
		return tokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(tokenConverter());
	}
}

