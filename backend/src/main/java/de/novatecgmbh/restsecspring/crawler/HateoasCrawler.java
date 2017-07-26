package de.novatecgmbh.restsecspring.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HateoasCrawler {

    private static Logger logger = LoggerFactory.getLogger(HateoasCrawler.class);
    private int numberOfEndpoints = 0;

    public HateoasCrawler(String url) {
        validateURL(url);
        crawl(url);
    }

    private void crawl(String url) {
        logger.info("Crawling " + url + " ...");
        validateURL("");
        this.numberOfEndpoints = 13;
    }

    private void validateURL(String url) {
        logger.info("Checking pattern of URL ...");
        logger.info("Target reachable? Sending ping ...");
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

}
