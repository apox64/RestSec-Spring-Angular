package de.novatecgmbh.restsecspring.api.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatecgmbh.restsecspring.logic.crawler.HateoasCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

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
