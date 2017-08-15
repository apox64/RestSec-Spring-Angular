package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AttackableEndpoint {

    private UUID id;
    private String httpVerb = "GET";
    private String endpointUrl = "";
    private boolean scanStatus = false;

    private static Logger logger = LoggerFactory.getLogger(AttackableEndpoint.class);

    public AttackableEndpoint() {
        this.id = UUID.randomUUID();
        logger.info("New AttackableEndpoint: " + this.id);
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

    public void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public void setEndpointURL(String endpointUrl) {
        this.endpointUrl = endpointUrl;
//        logger.info("endpointURL set: " + this.endpointURL);
    }

    public boolean getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(boolean scanStatus) {
        this.scanStatus = scanStatus;
//        logger.info("scanStatus set: " + this.scanStatus);

    }


}
