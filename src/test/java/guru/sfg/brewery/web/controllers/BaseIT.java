package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;

public abstract class BaseIT {
	
	@Autowired
	WebApplicationContext wac;
	
	protected MockMvc mockMvc;
	
//	@MockBean
//	BeerRepository beerRepository;
//	
//	@MockBean
//	BeerInventoryRepository beerInventoryRepository;
//	
//	@MockBean
//	CustomerRepository customerRepository;
//	
//	@MockBean
//	BreweryService breweryService;
//
//	@MockBean
//	BeerService beerService;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.apply(springSecurity())
				.build();
	}

	public static Stream<Arguments> getStreamAllUsers() {
		return Stream.of(Arguments.of("Florin", "password"),
				Arguments.of("user", "password"), 
				Arguments.of("vasile", "mypasss"));
	}

	public static Stream<Arguments> getStreamNotAdmin() {
		return Stream.of(Arguments.of("user", "password"),
				Arguments.of("vasile", "mypasss"));
	}

}
