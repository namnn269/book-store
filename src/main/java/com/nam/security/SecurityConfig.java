package com.nam.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nam.exception_mesage.MyAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests(auth -> auth
					.antMatchers("/admin/**").hasAuthority("ADMIN")
					.antMatchers("/trang-chu/**","/").permitAll()
					.antMatchers("/login","/access-denied","/logout","/register/**").permitAll()
					.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/error", "/dist/**")
						.permitAll()
					.anyRequest().authenticated())
			.formLogin(f->f
					.loginPage("/login")
					.loginProcessingUrl("/login")
//					.defaultSuccessUrl("/trang-chu",true)
					.failureUrl("/login?error=true")
					.failureHandler(null)
					.passwordParameter("password")
					.usernameParameter("username"))
			.logout(lo->lo
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login")
					.deleteCookies("JSESSIONID"))
			.rememberMe(rm->rm
					.key("theKey")
					.rememberMeParameter("remember-me")
					.tokenValiditySeconds(10))
			.exceptionHandling(exc->exc
					.accessDeniedHandler(myAccessDeniedHandler)
//					.accessDeniedPage("/login/handler")
					)
			.userDetailsService(userDetailsService);
		
//		http.authorizeRequests().antMatchers("/**").permitAll();
				
		return http.build();
	}
	
	@Bean
	public DaoAuthenticationProvider provider() {
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

}














