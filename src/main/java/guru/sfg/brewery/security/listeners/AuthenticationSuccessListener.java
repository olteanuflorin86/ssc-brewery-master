package guru.sfg.brewery.security.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationSuccessListener {

	@EventListener
	public void listen(AuthenticationSuccessEvent event) {
		
		log.debug("Usser Logged In Okay");
		
	}
}
