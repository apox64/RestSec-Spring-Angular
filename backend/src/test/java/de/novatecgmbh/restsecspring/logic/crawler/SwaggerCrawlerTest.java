package de.novatecgmbh.restsecspring.logic.crawler;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerCrawlerTest {

    private static final String FILE_LOCATION = "src/main/resources/uploads/api-swagger";

    @Test
    void crawlJson() {
        SwaggerCrawler swaggerCrawler = new SwaggerCrawler();
        swaggerCrawler.crawl(FILE_LOCATION + ".json");
    }

    @Test
    void crawlYaml() {
        SwaggerCrawler swaggerCrawler = new SwaggerCrawler();
        swaggerCrawler.crawl(FILE_LOCATION + ".yaml");
    }

    @Test
    void jsonYamlSameAttacksetLength() {
        crawlJson();
        int attacksetSizeBefore = Attackset.getInstance().getAttackSet().length();
        crawlYaml();
        int attacksetSizeAfter = Attackset.getInstance().getAttackSet().length();
        assertEquals(attacksetSizeBefore, attacksetSizeAfter);
    }

}