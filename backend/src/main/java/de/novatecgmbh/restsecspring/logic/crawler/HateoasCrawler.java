package de.novatecgmbh.restsecspring.logic.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.novatecgmbh.restsecspring.logic.HttpUtils;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import io.restassured.http.Header;

public class HateoasCrawler implements Crawler {

    private static Logger logger = LoggerFactory.getLogger(HateoasCrawler.class);
    private int numberOfEndpoints = 0;

    @JsonProperty("entrypointUrl")
    private String entrypointUrl;

    @JsonProperty("targetAuthToken")
    private String targetAuthToken;

    private HttpUtils httpUtils = new HttpUtils();

    public HateoasCrawler() {
        this.entrypointUrl = "";
        this.targetAuthToken = "";
    }

    public void crawl() {
        crawl(entrypointUrl);
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    private void setNumberOfEndpoints(int numberOfEndpoints) {
        this.numberOfEndpoints = numberOfEndpoints;
    }

    @Override
    public void crawl(String entryPoint) {
        logger.info("Crawling " + entryPoint + " ...");
        if (httpUtils.checkIfOnline(entryPoint)) {
            discoverLinks(entryPoint);
            urls.forEach((url, isVisited) -> Attackset.getInstance().add(new AttackableEndpoint(url, "GET")));
        }
    }

    private Map<String, Boolean> urls = new HashMap<>();

    private void discoverLinks(String entryResource) {
        List<String> initLinks = getSameOriginUrlsForUrl(entryResource);
        urls.put(entryResource, true);

        initLinks.forEach(url -> urls.put(url, false));

        while (urls.containsValue(false)) {
            urls.forEach((url, isVisited) -> {
                if (!isVisited) {
                    List<String> linksList = getSameOriginUrlsForUrl(url);
                    Map<String, Boolean> linksMap = markAllAsNotVisited(linksList);
                    urls.put(url, true);
                    urls = mergeHashMaps(urls, linksMap);
                }
            });
        }

        urls.forEach((url, isVisited) -> logger.info(url + " : " + isVisited));
        setNumberOfEndpoints(urls.size());
        logger.info(String.valueOf(urls.size()));
    }

    Map<String, Boolean> markAllAsNotVisited(List<String> list) {
        Map<String, Boolean> map = new HashMap<>();
        list.forEach(entry -> map.put(entry, false));
        return map;
    }

    List<String> getSameOriginUrlsForUrl(String url) {
        logger.info("Looking for Urls on: " + url);

        String responseBody = given().header(new Header("Authorization", "Bearer " + this.targetAuthToken)).get(url).asString();

        List<String> allUrls = matchAllUrlsInString(responseBody);
        List<String> sameOriginUrls;
        try {
            sameOriginUrls = matchSameOriginUrlsInList(allUrls, new URL(url));
        } catch (MalformedURLException e) {
            //e.printStackTrace();
            logger.info("Malformed URL.");
            return new ArrayList<>();
        }
        return sameOriginUrls;
    }

    List<String> matchAllUrlsInString(String responseBody) {
        List<String> allUrls = new ArrayList<>();
        //Pattern matches all URLs.
        Pattern patternFullURL = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}/([a-zA-Z0-9@:%._+-~#=?&/]*)");
        Matcher matcherFullURL = patternFullURL.matcher(responseBody);
        while (matcherFullURL.find()) {
            allUrls.add(matcherFullURL.group());
        }
        logger.info(allUrls.size() + " found.");
        return allUrls;
    }

    List<String> matchSameOriginUrlsInList(List<String> allUrls, URL originUrl) {
        logger.info("Matching only same-origin URLs from all URLs found on " + originUrl + " (size: " + allUrls.size() + ") ...");
        List<String> sameOriginUrls = new ArrayList<>();
        //Pattern matches Host and Port only to determine same-origin.
        Pattern pattern = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}(:?\\d+)/");
        Matcher matcher = pattern.matcher(originUrl.toString());

        String originUrlHostAndPortOnly = "";
        if (matcher.find()) {
            originUrlHostAndPortOnly = matcher.group();
        }

        String finalOriginUrlHostAndPortOnly = originUrlHostAndPortOnly;

        allUrls.forEach(url -> {
            Matcher m = pattern.matcher(url);
            if (m.find()) {
                if (m.group().equals(finalOriginUrlHostAndPortOnly)) {
                    sameOriginUrls.add(url);
                }
            }
        });
        logger.info(sameOriginUrls.size() + " found.");
        return sameOriginUrls;
    }

    public String getEntrypointUrl() {
        return entrypointUrl;
    }

    public void setEntrypointUrl(String entrypointUrl) {
        logger.info("entrypointUrl set to: " + entrypointUrl);
        this.entrypointUrl = entrypointUrl;
    }

    public String getTargetAuthToken() {
        return targetAuthToken;
    }

    public void setTargetAuthToken(String targetAuthToken) {
        logger.info("targetAuthToken set to: " + targetAuthToken);
        this.targetAuthToken = targetAuthToken;
    }

    private Map<String, Boolean> mergeHashMaps(Map<String, Boolean> existingMap, Map<String, Boolean> newMap) {
        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.putAll(newMap);
        resultMap.putAll(existingMap);
        return resultMap;
    }

}


