package guru.sfg.brewery.web.controllers.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import guru.sfg.brewery.web.controllers.BaseIT;

//@WebMvcTest
@SpringBootTest	
public class BeerRestControllerIT extends BaseIT {
	
	@Test
	void deleteBeerBadCreds() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3")
				.header("Api-Key", "Florin")
				.header("Api-Secret", "passwordXXXXXX"))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void deleteBeer() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3")
				.header("Api-Key", "Florin")
				.header("Api-Secret", "password"))
				.andExpect(status().isOk());
	}
	
	@Test
	void deleteBeerHttpBasic() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3")
				.with(httpBasic("Florin", "password")))
				.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	void deleteBeerNoAuth() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3"))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void findBeers() throws Exception {
		mockMvc.perform(get("/api/v1/beer/"))
				.andExpect(status().isOk());
	}
	
	@Test
	void findBeerById() throws Exception {
		mockMvc.perform(get("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3"))
				.andExpect(status().isOk());
	}
	
	@Test
	void findBeerByUpc() throws Exception {
		mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
				.andExpect(status().isOk());
	}
	
}
