package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
        .authorizeRequests(authorize -> {
            authorize
            	.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
//            	.antMatchers("/beers/find", "/beers*").permitAll()
            	.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
            	.antMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
        } )
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin().and()
        .httpBasic();
	}

	
	@Bean
	PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
//		return new LdapShaPasswordEncoder();
//		return new StandardPasswordEncoder();
		return new BCryptPasswordEncoder();
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("Florin")
//			.password("{noop}password")
//			.roles("ADMIN")
//			.and()
//			.withUser("user")
//			.password("{noop}password")
//			.roles("USER");
//	
//		auth.inMemoryAuthentication().withUser("vasile").password("{noop}mypasss").roles("CUSTOMER");
//	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("Florin")
			.password("password")
			.roles("ADMIN")
			.and()
			.withUser("user")
//			.password("password")
//			.password("{SSHA}fjNOxLGrlmnkhNYIgpgRDqxGLwHM8DUbnsjhdw==")
//			.password("533b63ea3057d21f5504ad8b0146a90963c0cda0bbb83543cf34679910838ebd287d65c6274a61cf")
			.password("$2a$16$n/XCvsNOhBXTXBHq6gcC1eGuh/vo1VCqeyBHMPAIge1Eb2GFJK.xW")
			.roles("USER");
		
		auth.inMemoryAuthentication().withUser("vasile").password("mypasss").roles("CUSTOMER");
	}


//	@Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("Florin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

}
