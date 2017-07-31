package de.novatecgmbh.restsecspring.api.scanner;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ZapController.class)
@WebAppConfiguration
@ContextConfiguration(classes = ZapController.class)
class ZapControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void zapStatus() throws Exception {
        mockMvc.perform(get("/scanner/zap/status")).andExpect(status().isOk());
    }

    @Test
    public void zapStart() throws Exception {
        mockMvc.perform(get("/scanner/zap/start")).andExpect(status().isOk());
    }

}