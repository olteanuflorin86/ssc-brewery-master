package guru.sfg.brewery.web.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

//@WebMvcTest
@SpringBootTest
public class BeerControllerIT extends BaseIT {
	
	@Test
    void initCreationFormWithFlorin() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("Florin", "password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }
	
	@Test
    void initCreationForm() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("user", "password")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }
	
	@Test
    void initCreationFormWithVasile() throws Exception {
        mockMvc.perform(get("/beers/new").with(httpBasic("vasile", "mypasss")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }
	
	@WithMockUser("Florin2")
	@Test
	void findBeers() throws Exception{
		mockMvc.perform(get("/beers/find"))
					.andExpect(status().isOk())
					.andExpect(view().name("beers/findBeers"))
					.andExpect(model().attributeExists("beer"));
	}

//	@Test
//	void findBeersWithAnonymous() throws Exception{
//		mockMvc.perform(get("/beers/find").with(anonymous()))
//					.andExpect(status().isOk())
//					.andExpect(view().name("beers/findBeers"))
//					.andExpect(model().attributeExists("beer"));
//	}
	
	@Test
	void findBeersWithHttpBasic() throws Exception{
		mockMvc.perform(get("/beers/find").with(httpBasic("Florin", "password")))
					.andExpect(status().isOk())
					.andExpect(view().name("beers/findBeers"))
					.andExpect(model().attributeExists("beer"));
	}
}
