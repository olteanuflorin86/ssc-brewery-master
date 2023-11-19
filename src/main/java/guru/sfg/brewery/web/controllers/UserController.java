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
	

	@PostMapping
	public String confirm2Fa(@RequestParam Integer verifyCode) {
		
		User user = getUser();
		
		log.debug("Entered Code is:" + verifyCode);
		
		if(googleAuthenticator.authorizeUser(user.getUsername(), verifyCode)) {
			User savedUser = userRepository.findById(user.getId()).orElseThrow();
			savedUser.setUserGoogle2fa(true);
			userRepository.save(savedUser);
		
			return "/index";
		} else {
			// bad code
			return "user/register2fa";
		}
	}
	
	@GetMapping("/verify2fa")
	public String verify2fa() {
		return "user/verify2fa";
	}
	
	@PostMapping
//	@PostMapping("/verify2fa")
	public String verifyPostOf2Fa(@RequestParam Integer verifyCode) {
		System.out.println("verifyCode: " + verifyCode);
		User user = getUser();
		
		if(googleAuthenticator.authorize(user.getUsername(), verifyCode)) {
			((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setGoogle2faRequired(false);
			return "/index";
		} else {
			return "user/verify2fa";
		}		
	}
	
	private User getUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
