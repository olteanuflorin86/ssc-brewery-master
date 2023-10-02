package guru.sfg.brewery.bootstrap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void loadSecurityData() {
//        Authority admin = authorityRepository.save(Authority.builder().role("ADMIN").build());
//        Authority userRole = authorityRepository.save(Authority.builder().role("USER").build());
//        Authority customer = authorityRepository.save(Authority.builder().role("CUSTOMER").build());
    	    	
//        Authority admin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
//        Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
//        Authority customer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());

    	// beer auths:
    	Authority createBeer = authorityRepository.save(Authority.builder().permission("beer.create").build());
    	Authority updateBeer = authorityRepository.save(Authority.builder().permission("beer.update").build());
    	Authority readBeer = authorityRepository.save(Authority.builder().permission("beer.read").build());
    	Authority deleteBeer = authorityRepository.save(Authority.builder().permission("beer.delete").build());
    	
    	// customer auths:
    	Authority createCustomer = authorityRepository.save(Authority.builder().permission("customer.create").build());
    	Authority updateCustomer = authorityRepository.save(Authority.builder().permission("customer.update").build());
    	Authority readCustomer = authorityRepository.save(Authority.builder().permission("customer.read").build());
    	Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("customer.delete").build());
    	
    	// customer brewery:
    	Authority createBrewery = authorityRepository.save(Authority.builder().permission("brewery.create").build());
    	Authority updateBrewery = authorityRepository.save(Authority.builder().permission("brewery.update").build());
    	Authority readBrewery = authorityRepository.save(Authority.builder().permission("brewery.read").build());
    	Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("brewery.delete").build());
    	
    	Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
    	Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
    	Role userRole = roleRepository.save(Role.builder().name("USER").build());
    	
    	adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer));
//    	customerRole.setAuthorities(Set.of(createBeer));
//    	userRole.setAuthorities(Set.of(createBeer));
    	adminRole.setAuthorities(new HashSet<>(Set.of(createBeer, updateBeer, readBeer, deleteBeer,
    			createCustomer, updateCustomer, readCustomer, deleteCustomer, 
    			createBrewery, updateBrewery, readBrewery, deleteBrewery)));
    	customerRole.setAuthorities(new HashSet<>(Set.of(createBeer, createCustomer, createBrewery)));
    	userRole.setAuthorities(new HashSet<>(Set.of(createBeer, createCustomer, createBrewery)));
    	
    	roleRepository.saveAll(Arrays.asList(adminRole, customerRole, userRole));
    	
        userRepository.save(User.builder()
                .username("Florin")
                .password(passwordEncoder.encode("password"))
//                .authority(admin)
                .role(adminRole)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
//                .authority(userRole)
                .role(userRole)
                .build());

        userRepository.save(User.builder()
                .username("vasile")
                .password(passwordEncoder.encode("mypasss"))
//                .authority(customer)
                .role(customerRole)
                .build());

        log.debug("Users Loaded: " + userRepository.count());
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }

}