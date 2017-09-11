package de.novatecgmbh.restsecspring.logic.crawler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HateoasCrawlerTest {

    private static final Logger logger = LoggerFactory.getLogger(HateoasCrawlerTest.class);
    private HateoasCrawler hateoasCrawler;
    private static final String HATEOAS_TARGET = "http://localhost:10001/albums/";

    @BeforeEach
    void setUp() {
        this.hateoasCrawler = new HateoasCrawler();
    }

    @AfterEach
    void tearDown() {
    }

    @Disabled("Integration Test")
    @Test
    void getSameOriginUrlsForUrl() {
        List<String> sameOriginUrls = this.hateoasCrawler.getSameOriginUrlsForUrl(HATEOAS_TARGET);
        assertEquals(6, sameOriginUrls.size());
    }

    @Test
    void matchAllUrlsInString() {
        String responseBody = "<href=\"http://mytestsite.com:8081/test/api\">[{\"id\":\"1\",\"title\":\"Heritage\",\"artist\":{\"id\":\"opeth\",\"name\":\"Opeth\"},\"stockLevel\":2,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/1\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/opeth\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/1\"}]},{\"id\":\"2\",\"title\":\"Deliverance\",\"artist\":{\"id\":\"opeth\",\"name\":\"Opeth\"},\"stockLevel\":3,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/2\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/opeth\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/2\"}]},{\"id\":\"3\",\"title\":\"Monotheist\",\"artist\":{\"id\":\"cfrost\",\"name\":\"Celtic Frost\"},\"stockLevel\":1,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/3\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/cfrost\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/3\"}]}]<href=\"http://api.someotherapi.net/v2/api/\">";
        List<String> allUrls = this.hateoasCrawler.matchAllUrlsInString(responseBody);
        allUrls.forEach(logger::info);
        assertEquals(11, allUrls.size());
    }

    @Test
    void matchSameOriginUrlsInList() {
        URL entryResourceUrl = null;
        try {
            entryResourceUrl = new URL("http://localhost:10001/albums/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseBody = "<href=\"http://mytestsite.com:8081/test/api\">[{\"id\":\"1\",\"title\":\"Heritage\",\"artist\":{\"id\":\"opeth\",\"name\":\"Opeth\"},\"stockLevel\":2,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/1\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/opeth\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/1\"}]},{\"id\":\"2\",\"title\":\"Deliverance\",\"artist\":{\"id\":\"opeth\",\"name\":\"Opeth\"},\"stockLevel\":3,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/2\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/opeth\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/2\"}]},{\"id\":\"3\",\"title\":\"Monotheist\",\"artist\":{\"id\":\"cfrost\",\"name\":\"Celtic Frost\"},\"stockLevel\":1,\"links\":[{\"rel\":\"self\",\"href\":\"http://localhost:10001/album/3\"},{\"rel\":\"artist\",\"href\":\"http://localhost:10001/artist/cfrost\"},{\"rel\":\"album.purchase\",\"href\":\"http://localhost:10001/album/purchase/3\"}]}]<href=\"http://api.someotherapi.net/v2/api/\">";
        List<String> allUrls = this.hateoasCrawler.matchAllUrlsInString(responseBody);
        List<String> sameOriginUrls = this.hateoasCrawler.matchSameOriginUrlsInList(allUrls, entryResourceUrl);
        sameOriginUrls.forEach(logger::info);
        assertEquals(9, sameOriginUrls.size());
    }

    @Test
    void crawl() {
        hateoasCrawler.crawl("http://localhost:10001/albums");
    }

}