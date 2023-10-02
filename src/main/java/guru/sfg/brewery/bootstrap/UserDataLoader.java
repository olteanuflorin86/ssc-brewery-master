package guru.sfg.brewery.bootstrap;

import java.util.Arrays;
import java.util.Set;

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
    	
    	Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
    	Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());
    	Role userRole = roleRepository.save(Role.builder().name("USER").build());
    	
    	adminRole.setAuthorities(Set.of(createBeer, updateBeer, readBeer, deleteBeer));
    	customerRole.setAuthorities(Set.of(createBeer));
    	userRole.setAuthorities(Set.of(createBeer));
    	
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

    @Override
    public void run(String... args) throws Exception {
        if (authorityRepository.count() == 0) {
            loadSecurityData();
        }
    }

}