package de.novatecgmbh.restsecspring.api.crawler;

import de.novatecgmbh.restsecspring.logic.crawler.HateoasCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/crawler/hateoas")
public class HateoasCrawlerController {

    private static Logger logger = LoggerFactory.getLogger(HateoasCrawlerController.class);

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String hateoas(@RequestParam Map<String, String> requestParams){

        String url = requestParams.get("url");
        String targetAuthToken = requestParams.get("targetAuthToken");

        logger.info("targetUrl: " + url);
        logger.info("targetAuthToken: " + targetAuthToken);

        HateoasCrawler hateoasCrawler = new HateoasCrawler(url);
        return String.valueOf(hateoasCrawler.getNumberOfEndpoints());
    }
}
