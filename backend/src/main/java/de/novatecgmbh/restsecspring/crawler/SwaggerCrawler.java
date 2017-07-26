package de.novatecgmbh.restsecspring.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwaggerCrawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);

    public SwaggerCrawler() {
        crawl();
    }

    private void crawl() {
        logger.info("Crawling a file ...");
        // do some file magic here ...
        this.numberOfEndpoints = 5;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

}
