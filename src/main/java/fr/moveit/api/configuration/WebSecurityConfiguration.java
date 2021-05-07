package fr.moveit.api.configuration;


import fr.moveit.api.configuration.provider.CASAuthentificationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	final CASAuthentificationProvider casAuthenticationProvider;

	public WebSecurityConfiguration(CASAuthentificationProvider casAuthenticationProvider) {
		this.casAuthenticationProvider = casAuthenticationProvider;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/oauth/**").permitAll();
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
