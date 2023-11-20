package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;    
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.google.Google2faFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final PersistentTokenRepository persistentTokenRepository;
	private final Google2faFilter google2faFilter;
	
	// need for use with Spring Data JPA SPeL
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
	
//	public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
//		RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
//		filter.setAuthenticationManager(authenticationManager);
//		return filter;
//	}
	
//	public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager){
//        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

//		http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
//				UsernamePasswordAuthenticationFilter.class)
//			.csrf().disable();	
		
//		http.addFilterBefore(restUrlAuthFilter(authenticationManager()),
//                UsernamePasswordAuthenticationFilter.class);	

		http.addFilterBefore(google2faFilter, SessionManagementFilter.class);
		
		http
        .authorizeRequests(authorize -> {
            authorize
            .antMatchers("/h2-console/**").permitAll() //do not use in production!
            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
//            .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
//                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
//            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
//            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
//                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
//            .mvcMatchers("/brewery/breweries")
//                .hasAnyRole("ADMIN", "CUSTOMER")
//            .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
//                .hasAnyRole("ADMIN", "CUSTOMER")
//            .mvcMatchers("/beers/find", "/beers/{beerId}")
//                .hasAnyRole("ADMIN", "CUSTOMER", "USER");
        } )
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
//        .formLogin().and()
        .formLogin(loginConfigurer -> {
            loginConfigurer
                    .loginProcessingUrl("/login")
                    .loginPage("/").permitAll()
                    .successForwardUrl("/")
                    .defaultSuccessUrl("/")
            		.failureUrl("/?error");
        })
         .logout(logoutConfigurer -> {
             logoutConfigurer
                     .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                     .logoutSuccessUrl("/?logout")
                     .permitAll();
         })
        .httpBasic()
//        .and().csrf().disable();	
        .and().csrf().ignoringAntMatchers("/h2-console/**"/*, "/api/**"*/)
//        .and().rememberMe()
//	        .key("sfg-key")
//	        .userDetailsService(userDetailsService);
        .and().rememberMe()
        .tokenRepository(persistentTokenRepository)
        .userDetailsService(userDetailsService);
		
		//h2 console config
		http.headers().frameOptions().sameOrigin();
		
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
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//			.withUser("Florin")
////			.password("password")
//			.password("{bcrypt}$2a$16$miFBL.a4A4ogfYgr7.zCf.Gl1y32tp6xWspl6E9L1M1b6hzb97quq")
//			.roles("ADMIN")
//			.and()
//			.withUser("user")
////			.password("password")
////			.password("{SSHA}fjNOxLGrlmnkhNYIgpgRDqxGLwHM8DUbnsjhdw==")
////			.password("533b63ea3057d21f5504ad8b0146a90963c0cda0bbb83543cf34679910838ebd287d65c6274a61cf")
////			.password("$2a$16$n/XCvsNOhBXTXBHq6gcC1eGuh/vo1VCqeyBHMPAIge1Eb2GFJK.xW")
//			.password("{sha256}f8dab4036d93d4304be916fdf4bb34647796e395dd8fb51c7a5dd30a7c088540d1962dab23b89ea8")
//			.roles("USER");
//		
////		auth.inMemoryAuthentication().withUser("vasile").password("mypasss").roles("CUSTOMER");
//		auth.inMemoryAuthentication().withUser("vasile").password("{ldap}{SSHA}N4hMFDeHXDYrOUk597IIqG8FbRV2cbwZl7KTKA==").roles("CUSTOMER");
//		
//	}


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
