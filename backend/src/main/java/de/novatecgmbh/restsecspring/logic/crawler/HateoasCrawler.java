package de.novatecgmbh.restsecspring.logic.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.novatecgmbh.restsecspring.logic.HttpUtils;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Override
    public void crawl(String entryPoint) {
        logger.info("Crawling " + entryPoint + " ...");
        if (httpUtils.isOnline(entryPoint)) {
            discoverLinks(entryPoint);
        }
    }

//    private class Endpoint {
//        private String url;
//        private boolean isAlreadyVisited;
//    }

    //PROTOTYPE
    private void scanRecursively() {
        //Map<entrypointUrl, isAlreadyVisited>
        Map<String, Boolean> endpoints = new HashMap<>();

        //as long as there are unscanned endpoints ...

    }

    private void discoverLinks(String entryResource) {
        Map<String, Boolean> relevantURLs = getLinksForResource(entryResource);
        relevantURLs.put(entryResource, true);

        while (relevantURLs.containsValue(false)) {
            for (Object o : relevantURLs.entrySet()) {
                Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) o;
                if (!pair.getValue())
                    if (!Boolean.parseBoolean(pair.getValue().toString())) {
                        Map<String, Boolean> temp;
                        temp = getLinksForResource(pair.getKey().toString());
                        relevantURLs = mergeHashMaps(relevantURLs, temp);
                    }
            }
        }

        numberOfEndpoints = relevantURLs.keySet().size();

        Attackset attackSet = Attackset.getInstance();
        for (String url : relevantURLs.keySet()) {
            AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
            attackableEndpoint.setEndpointURL(url);
            attackSet.add(attackableEndpoint);
        }

    }

    //TODO: change to List<String> instead of Map<String, Boolean>
    private Map<String, Boolean> getLinksForResource(String resource) {
        logger.info("Looking for Links on: " + resource);

        String responseBody = given().header(new Header("Authorization", "Bearer " + this.targetAuthToken)).get(resource).asString();

        List<String> allUrls = getAllUrlsForGivenResponseBody(responseBody);
        List<String> sameOriginUrls = getSameOriginUrlsForGivenUrlList(allUrls);

        Map<String, Boolean> res = new HashMap<>();
        sameOriginUrls.forEach(url -> res.put(url, false));
        return res;
    }

    private List<String> getAllUrlsForGivenResponseBody(String responseBody) {
        List<String> allUrls = new ArrayList<>();
        Pattern patternFullURL = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}/([a-zA-Z0-9@:%._+-~#=?&/]*)");
        Matcher matcherFullURL = patternFullURL.matcher(responseBody);
        while (matcherFullURL.find()) {
            allUrls.add(matcherFullURL.group());
        }
        return allUrls;
    }

    private List<String> getSameOriginUrlsForGivenUrlList(List<String> urls) {
        List<String> sameOriginUrls = new ArrayList<>();
        Pattern patternHostAndPortOnly = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}(:?\\d+)/");
        urls.forEach(url -> {
            Matcher matcher = patternHostAndPortOnly.matcher(url);
            if (matcher.find()) {
                sameOriginUrls.add(matcher.group());
            }
        });
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

    private Map<String, Boolean> mergeHashMaps(Map<String, Boolean> map1, Map<String, Boolean> map2) {
        Map<String, Boolean> resultMap = new HashMap<>();
        resultMap.putAll(map1);
        resultMap.putAll(map2);
        return resultMap;
    }

}


