package de.novatecgmbh.restsecspring.logic.crawler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

class HateoasCrawlerTest {
    @Test
    void crawl() {
        HateoasCrawler hateoasCrawler = new HateoasCrawler();
        hateoasCrawler.crawl("http://localhost:10001/albums");
    }

}