package com.qumla.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebMvcSecurity
@ComponentScan
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	@Autowired
	private SecurityContextRepository securityRepo;
	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		logger.debug("set AuthenticationProvider:"+authenticationProvider.getClass());
		auth.authenticationProvider(authenticationProvider);
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.securityContext().securityContextRepository(securityRepo).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				.antMatchers(HttpMethod.GET,"/common/**").permitAll()
				.antMatchers(HttpMethod.POST,"/facebooklogin").permitAll()
				.antMatchers(HttpMethod.POST,"/search").permitAll()
				.antMatchers(HttpMethod.POST,"/googlelogin").permitAll()
				.antMatchers(HttpMethod.GET,"/foauth2callback").permitAll()
				.antMatchers(HttpMethod.GET,"/oauth2callback").permitAll()
				.antMatchers(HttpMethod.POST,"/foauth2tokencheck").permitAll()
				.antMatchers(HttpMethod.POST,"/oauth2tokencheck").permitAll()
				.antMatchers(HttpMethod.GET,"/profileimage").permitAll()
				.antMatchers(HttpMethod.GET,"/profile/setup").permitAll()
				.antMatchers(HttpMethod.POST,"/signup").permitAll()
				
				
				
				.antMatchers(HttpMethod.GET,"/countryresult").hasRole("USER") // should be user and advanced question or should be a customer to access
				.antMatchers(HttpMethod.GET,"/regionresult").hasRole("USER") // should be user and advanced question or should be a customer to access
				.antMatchers(HttpMethod.GET,"/locationresult").hasRole("CUSTOMER")
				.antMatchers(HttpMethod.GET,"/myoptionregionresult").hasRole("USER")
				.antMatchers(HttpMethod.GET,"/votingarea").hasRole("USER")
				.antMatchers(HttpMethod.GET,"/answerstathistory").hasRole("USER")
				
				
				

				
	 			.antMatchers("/japi/langtext").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST,"/japi/langtext/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT,"/japi/langtext/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.PATCH,"/japi/langtext/*").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,"/japi/langtext/*").hasRole("ADMIN")
				
				.antMatchers(HttpMethod.GET,"/locationmanager").hasRole("ADMIN")
				
				
				.antMatchers(HttpMethod.POST,"/uploadfile/**").hasRole("USER")
				.antMatchers(HttpMethod.GET,"/location").hasRole("USER")				
				.antMatchers(HttpMethod.POST,"/login").permitAll()
				.antMatchers(HttpMethod.POST,"/token").permitAll()
				.antMatchers(HttpMethod.GET,"/user/*").hasRole("USER")
				.antMatchers(HttpMethod.PUT,"/user/*").hasRole("USER")

				
				.antMatchers(HttpMethod.GET,"/loginTest").permitAll()
				
				.antMatchers(HttpMethod.GET,"/*").hasRole("USER")
				.antMatchers(HttpMethod.POST,"/*").hasRole("USER").anyRequest()
				.authenticated();
	}
}
