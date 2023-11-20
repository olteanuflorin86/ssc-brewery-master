package guru.sfg.brewery.security.google;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import guru.sfg.brewery.domain.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Google2faFilter extends GenericFilterBean {

	private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
	
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
			log.debug("Processing 2FA Filter");
		
			if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User) {
				User user = (User) authentication.getPrincipal();
				
				if(user.getUseGoogle2fa() && user.getGoogle2faRequired()) {
					log.debug("2FA Required");
					
					// to do add failure handle
					google2faFailureHandler.onAuthenticationFailure(request, response, null);
					
				}
			}
		
		}
		
		filterChain.doFilter(servletRequest, servletResponse);
	}

}
