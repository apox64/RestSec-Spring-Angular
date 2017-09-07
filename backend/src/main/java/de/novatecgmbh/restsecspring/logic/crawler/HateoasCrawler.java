package de.novatecgmbh.restsecspring.logic.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.novatecgmbh.restsecspring.logic.HttpUtils;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

import io.restassured.http.Header;

public class HateoasCrawler implements Crawler {

    private static Logger logger = LoggerFactory.getLogger(HateoasCrawler.class);
    private int numberOfEndpoints = 0;

    @JsonProperty("url")
    private String url;

    @JsonProperty("targetAuthToken")
    private String targetAuthToken;

    public HateoasCrawler() {
    }

    public void crawl() {
        crawl(url);
    }

    @Override
    public void crawl(String entryPoint) {
        logger.info("Crawling " + entryPoint + " ...");
        try {
            validateURL(url, targetAuthToken);
            discoverLinks(url, targetAuthToken);
        } catch (ConnectException e) {
            logger.warn(url + " not reachable.");
        } catch (MalformedURLException e) {
            logger.warn("Malformed URL.");
        }
    }

    private void validateURL(String url, String authToken) throws MalformedURLException {
        logger.info("Checking pattern of URL: \"" + url + "\" ...");
        URL myUrl = new URL(url);
        logger.info("URL (" + myUrl + ") seems good.");
        this.targetAuthToken = authToken;
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    private void discoverLinks(String entryResource, String authToken) throws ConnectException {
        Attackset attackSet = Attackset.getInstance();

        HashMap<String, Boolean> relevantURLs;
        relevantURLs = getLinksForResource(entryResource, "");
        relevantURLs.put(entryResource, true);

        while (relevantURLs.containsValue(false)) {
            for (Object o : relevantURLs.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                if (!Boolean.parseBoolean(pair.getValue().toString())) {
                    HashMap<String, Boolean> temp;
                    temp = getLinksForResource(pair.getKey().toString(), "");
                    relevantURLs = mergeHashMaps(relevantURLs, temp);
                }
            }
        }

        numberOfEndpoints = relevantURLs.keySet().size();

        for (String url : relevantURLs.keySet()) {
            AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
            attackableEndpoint.setEndpointURL(url.toString());
            attackSet.add(attackableEndpoint);
        }

    }

    private HashMap<String, Boolean> getLinksForResource(String resource, String authToken) {

        Pattern patternFullURL = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}/([a-zA-Z0-9@:%_+-.~#?&/=]*)");
        Pattern patternHostAndPortOnly = Pattern.compile("https?://(www\\.)?[a-zA-Z0-9@:%._+-~#=]{2,256}(:?\\d+)/");
        String responseBody =
                given().header(new Header("Authorization", "Bearer " + "")).
                        get(resource).asString();

        Matcher matcherFullURL = patternFullURL.matcher(responseBody);
        Matcher matcherHostAndPortOnly = patternHostAndPortOnly.matcher(resource);

        HashMap<String, Boolean> sameOriginURLsOnly = new HashMap<>();
        HashMap<String, Boolean> allURLsInResponse = new HashMap<>();

        while (matcherFullURL.find()) {
            allURLsInResponse.put(matcherFullURL.group(), false);
        }

        //Return only the URLs that are on the same host as the given resource
        matcherHostAndPortOnly.find();
        String entryResourceDomainAndPortOnly = matcherHostAndPortOnly.group();

        for (Object o : allURLsInResponse.entrySet()) {
            Map.Entry url = (Map.Entry) o;
            Matcher m = patternHostAndPortOnly.matcher(url.getKey().toString());
            while (m.find()) {
                if (m.group().equals(entryResourceDomainAndPortOnly)) {
                    sameOriginURLsOnly.put(url.getKey().toString(), false);
                }
            }
        }

        sameOriginURLsOnly.put(resource, true);

        return sameOriginURLsOnly;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        logger.info("url set to: " + url);
        this.url = url;
    }

    public String getTargetAuthToken() {
        return targetAuthToken;
    }

    public void setTargetAuthToken(String targetAuthToken) {
        logger.info("targetAuthToken set to: " + targetAuthToken);
        this.targetAuthToken = targetAuthToken;
    }

    private HashMap<String, Boolean> mergeHashMaps(HashMap<String, Boolean> map1, HashMap<String, Boolean> map2) {
        HashMap<String, Boolean> resultMap = new HashMap<>();
        resultMap.putAll(map1);
        resultMap.putAll(map2);
        return resultMap;
    }

}
