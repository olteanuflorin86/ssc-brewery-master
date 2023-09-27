package guru.sfg.brewery.web.controllers.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import guru.sfg.brewery.web.controllers.BaseIT;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

	@Test
	void deleteBeer() throws Exception {
		mockMvc.perform(delete("/api/v1/beer/e58ed763-928c-4155-bee9-fdbaaadc15f3")
				.header("Api-Key", "Florin")
				.header("Api-Secret", "password"))
				.andExpect(status().isOk());
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
