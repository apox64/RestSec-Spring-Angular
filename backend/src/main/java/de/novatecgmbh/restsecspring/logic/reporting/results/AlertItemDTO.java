package de.novatecgmbh.restsecspring.logic.reporting.results;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "alertitem")
public class AlertItemDTO {

    private String alert;
    private String name;
    private int riskcode;
    private int confidence;
    private String desc;
    private String uri;
    private String method;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRiskcode() {
        return riskcode;
    }

    public void setRiskcode(int riskcode) {
        this.riskcode = riskcode;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
