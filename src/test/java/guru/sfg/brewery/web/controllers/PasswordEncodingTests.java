package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

public class PasswordEncodingTests {

	static final String PASSWORD = "password";
	
//	@Test
//	void testLdap() {
//		PasswordEncoder ldap = new LdapShaPasswordEncoder();
//		
//		System.out.println(ldap.encode(PASSWORD));
//		System.out.println(ldap.encode(PASSWORD));
//	}
	
	@Test
	void hashingExample() {
		System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
		
		String salted = PASSWORD + "ThisIsMySALTVALUE";
		System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
	}
	
	@Test
	void testNoOp() {
		PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
		
		System.out.println(noOp.encode(PASSWORD));
	}
}
