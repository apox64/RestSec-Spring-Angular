package de.novatecgmbh.restsecspring.api.reporting;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@WebAppConfiguration
class AttacksetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private WebApplicationContext context;

    @Test
    public void reachable() throws Exception {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context);
        mockMvc.perform(get("/reporting/attackset"))
                .andExpect(status().isOk());
    }

}