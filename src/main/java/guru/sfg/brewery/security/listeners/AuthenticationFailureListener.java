package guru.sfg.brewery.security.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener {
	
	private final LoginFailureRepository loginFailureRepository;
	private final UserRepository userRepository;
	
	@EventListener
	public void listen(AuthenticationFailureBadCredentialsEvent event) {
	
		log.debug("Login failure");
		
		if(event.getSource() instanceof UsernamePasswordAuthenticationToken) {			
			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();
			
			LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
			
			if(token.getPrincipal() instanceof String) {
				
				log.debug("Attempted Username: " + token.getPrincipal());
				
				builder.username((String) token.getPrincipal());
				userRepository.findByUsername((String) token.getPrincipal()).ifPresent(builder::user);
				
			}
			
			if(token.getDetails() instanceof WebAuthenticationDetails) {
				WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();
				
				log.debug("Source IP: " + details.getRemoteAddress());
				
				builder.sourceIp(details.getRemoteAddress());
			}
			
			LoginFailure loginFailure =  loginFailureRepository.save(builder.build());
			log.debug("Failure Event: " + loginFailure.getId());
		}
	}

}
