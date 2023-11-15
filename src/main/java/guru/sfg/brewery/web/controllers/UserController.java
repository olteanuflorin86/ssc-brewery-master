package guru.sfg.brewery.web.controllers;

import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

	private final UserRepository userRepository; 
	private final GoogleAuthenticator googleAuthenticator;
	
	@GetMapping("/register2fa")
	public String register2fa(Model model) {
		
		User user = getUser();
		
		String url = GoogleAuthenticatorQRGenerator.getOtpAuthURL("Florin", user.getUsername(), 
				googleAuthenticator.createCredentials(user.getUsername()));
		
		log.debug("Google QR URL: " + url);
		
//		model.addAttribute("googleurl", "todo");
		model.addAttribute("googleurl", url);
		
		return "user/register2fa";
	}
	
	private User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@PostMapping
	public String confirm2Fa(@RequestParam Integer verifyCode) {
		
		// todo - impl
		
		return "index";
	}
	
}
