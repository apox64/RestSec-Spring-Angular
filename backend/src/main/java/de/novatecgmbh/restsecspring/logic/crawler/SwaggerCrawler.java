package de.novatecgmbh.restsecspring.logic.crawler;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwaggerCrawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);

    private Attackset attackset;

    public SwaggerCrawler() {
        try {
            crawl();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void crawl() throws JSONException {
        logger.info("Crawling a file ...");
        attackset = Attackset.getInstance();
        AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
        attackableEndpoint.setEndpointURL("http://test.local");
        attackableEndpoint.setScanStatus(false);
        attackset.add(attackableEndpoint);
        // do some file magic here ...
//        this.numberOfEndpoints = getNumberOfEndpoints();
    }

//    public int getNumberOfEndpoints() {
//        return attackset.getAttackSet().length();
//    }

}
