package de.novatecgmbh.restsecspring.logic.reporting.attackset;

public class AttackableEndpoint {

    private String endpointURL;
    private boolean scanStatus;

    public String getEndpointURL() {
        return endpointURL;
    }

    public void setEndpointURL(String endpointURL) {
        this.endpointURL = endpointURL;
    }

    public boolean getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(boolean scanStatus) {
        this.scanStatus = scanStatus;
    }
}
