package fr.moveit.api.configuration;


import fr.moveit.api.security.jwt.JWTFilter;
import fr.moveit.api.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	final private JWTProvider JWTProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disable CSRF (cross site request forgery)
		http.csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Entry points
		http.authorizeRequests()//
				.antMatchers("/s/**").permitAll()
				// Disallow everything else..
				.anyRequest().authenticated();

		// If a user try to access a resource without having enough permissions
		//http.exceptionHandling().accessDeniedPage("/login");

		// Apply JWT
		http.addFilterBefore(new JWTFilter(JWTProvider), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web){
		// Allow swagger to be accessed without authentication
		web.ignoring().antMatchers("/v2/api-docs")//
				.antMatchers("/swagger-resources/**")//
				.antMatchers("/swagger-ui.html")//
				.antMatchers("/configuration/**")//
				.antMatchers("/webjars/**")//
				.antMatchers("/public")

				// Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
				.and()
				.ignoring()
				.antMatchers("/h2-console/**/**");;
	}


	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
}
