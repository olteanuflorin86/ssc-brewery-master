package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
		RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
				UsernamePasswordAuthenticationFilter.class);
		
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
//		return new BCryptPasswordEncoder();
		
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
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
//			.password("password")
			.password("{bcrypt}$2a$16$miFBL.a4A4ogfYgr7.zCf.Gl1y32tp6xWspl6E9L1M1b6hzb97quq")
			.roles("ADMIN")
			.and()
			.withUser("user")
//			.password("password")
//			.password("{SSHA}fjNOxLGrlmnkhNYIgpgRDqxGLwHM8DUbnsjhdw==")
//			.password("533b63ea3057d21f5504ad8b0146a90963c0cda0bbb83543cf34679910838ebd287d65c6274a61cf")
//			.password("$2a$16$n/XCvsNOhBXTXBHq6gcC1eGuh/vo1VCqeyBHMPAIge1Eb2GFJK.xW")
			.password("{sha256}f8dab4036d93d4304be916fdf4bb34647796e395dd8fb51c7a5dd30a7c088540d1962dab23b89ea8")
			.roles("USER");
		
//		auth.inMemoryAuthentication().withUser("vasile").password("mypasss").roles("CUSTOMER");
		auth.inMemoryAuthentication().withUser("vasile").password("{ldap}{SSHA}N4hMFDeHXDYrOUk597IIqG8FbRV2cbwZl7KTKA==").roles("CUSTOMER");
		
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
