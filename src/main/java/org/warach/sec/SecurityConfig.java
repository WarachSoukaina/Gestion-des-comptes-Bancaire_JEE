package org.warach.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = getBCPE();
		//System.out.println(encoder.encode("1234"));
		auth.inMemoryAuthentication().withUser("admin").password("$2a$10$RwuxVAXvH3mc7X5EIaqgAO1DBUXCET4zoVoJIQ5JUXunEhFnkMYai").roles("ADMIN","USER");
		auth.inMemoryAuthentication().withUser("user").password("$2a$10$RwuxVAXvH3mc7X5EIaqgAO1DBUXCET4zoVoJIQ5JUXunEhFnkMYai").roles("USER");

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login");
		http.authorizeRequests().antMatchers("/operations","/consulterCompte").hasRole("USER");
		http.authorizeRequests().antMatchers("/saveOperation").hasRole("ADMIN");
		 http.exceptionHandling().accessDeniedPage("/403");
	}
	
	@Bean
	public BCryptPasswordEncoder getBCPE() {
		return new BCryptPasswordEncoder();
	}
}
