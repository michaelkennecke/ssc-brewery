package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FindBeerControllerTest extends BaseIT {

    @Test
    void findBeerTest() throws Exception {
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk());
    }
}
