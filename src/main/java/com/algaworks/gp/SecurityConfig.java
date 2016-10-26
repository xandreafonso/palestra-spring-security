package com.algaworks.gp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/relatorio-custos").hasAnyRole("PG_CUSTOS")
				.antMatchers("/relatorio-equipe").hasAnyRole("PG_EQUIPE")
				.anyRequest()
				.authenticated()
			.and()
			.formLogin()
				.loginPage("/entrar")
				.permitAll()
			.and()
			.logout()
				.logoutSuccessUrl("/entrar?logout")
				.permitAll()
			.and()
			.rememberMe()
				.userDetailsService(gpUserDetailsService())
			;
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder, PasswordEncoder passwordEncoder, GpUserDetailsService userDetailsService) throws Exception {
		builder
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
	}
	
	@Bean
	public GpUserDetailsService gpUserDetailsService() {
		return new GpUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	}
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder builder, PasswordEncoder passwordEncoder, DataSource dataSource) throws Exception {
//		builder
//			.jdbcAuthentication()
//			.dataSource(dataSource)
//			.passwordEncoder(passwordEncoder)
//			.usersByUsernameQuery("select login, senha, ativo from usuario where login = ?")
//			.authoritiesByUsernameQuery("select u.login, up.permissao from usuario_permissao up join usuario u on u.id = up.usuario_id where u.login = ?");
//	}
	
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
//		builder
//			.inMemoryAuthentication()
//			.withUser("carlos").password("123").roles("PG_CUSTOS", "PG_EQUIPE")
//			.and()
//			.withUser("flavio").password("123").roles("PG_EQUIPE");
//	}
	
	
}
