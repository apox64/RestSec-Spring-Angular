package de.novatecgmbh.restsecspring.api.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatecgmbh.restsecspring.logic.crawler.HateoasCrawler;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

//MappingJackson2HttpMessageConverter

@EnableWebMvc
@Configuration
@RestController
@RequestMapping("/crawler/hateoas")
@CrossOrigin
public class HateoasCrawlerController {

    private static Logger logger = LoggerFactory.getLogger(HateoasCrawlerController.class);

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes = "application/json")
    public @ResponseBody int startHateoasCrawler(@RequestBody String requestBody){

        ObjectMapper objectMapper = new ObjectMapper();

        HateoasCrawler hateoasCrawler = null;

        try {
            hateoasCrawler = objectMapper.readValue(requestBody, HateoasCrawler.class);
            hateoasCrawler.crawl();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hateoasCrawler.getNumberOfEndpoints();

    }
}
