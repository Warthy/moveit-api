package fr.moveit.api.configuration;


import fr.moveit.api.configuration.provider.CASAuthentificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	final CASAuthentificationProvider casAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// don't need csrf because of token in header
			.csrf().disable()
			// don't create session
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()

			.authorizeRequests()
			.antMatchers("/oauth/**").permitAll()
			.anyRequest().authenticated();
	}

	@Bean()
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(casAuthenticationProvider);
	}
}
