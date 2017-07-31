package de.novatecgmbh.restsecspring.logic.crawler;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwaggerCrawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);

    private Attackset attackset;

    public SwaggerCrawler() {
        crawl();
    }

    private void crawl() {
        logger.info("Crawling a file ...");
        attackset = new Attackset();
        AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
        attackableEndpoint.setEndpointURL("http://test.local");
        attackableEndpoint.setScanStatus(false);
        attackset.add(4, attackableEndpoint);
        attackset.add(attackableEndpoint);
        attackset.add(attackableEndpoint);
        attackset.add(8, attackableEndpoint);
        attackset.add(attackableEndpoint);
        attackset.add(attackableEndpoint);
        // do some file magic here ...
        this.numberOfEndpoints = 5;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

}
