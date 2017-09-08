package de.novatecgmbh.restsecspring.logic.crawler;

public interface Crawler {

    void crawl(String entryPoint);

    int getNumberOfEndpoints();

}
