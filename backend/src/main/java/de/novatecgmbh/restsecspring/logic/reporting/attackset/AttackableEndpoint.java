package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AttackableEndpoint {

    private UUID id;

    @JsonProperty("httpVerb")
    private String httpVerb = "GET";

    @JsonProperty("endpointUrl")
    private String endpointUrl = "";

    @JsonProperty("authToken")
    private String authToken = "";

    @JsonProperty("scanStatus")
    private boolean scanStatus = false;

    private static Logger logger = LoggerFactory.getLogger(AttackableEndpoint.class);

    public AttackableEndpoint() {
        this.id = UUID.randomUUID();
    }

    public AttackableEndpoint(String endpointUrl, String httpVerb, String authToken) {
        this.id = UUID.randomUUID();
        this.endpointUrl = endpointUrl;
        this.httpVerb = httpVerb;
        this.authToken = authToken;
        this.scanStatus = false;
    }

    public UUID getId() {
        return id;
    }

    public String getEndpointURL() {
        return endpointUrl;
    }

    public String getHttpVerb() {
        return httpVerb;
    }

    public String getAuthToken() { return authToken; }

    public boolean getScanStatus() {
        return scanStatus;
    }

    public void setEndpointURL(String endpointUrl) {
        this.endpointUrl = endpointUrl;
//        logger.info("endpointURL set: " + this.endpointURL);
    }

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public void setScanStatus(boolean scanStatus) {
        this.scanStatus = scanStatus;
//        logger.info("scanStatus set: " + this.scanStatus);

    }

}
