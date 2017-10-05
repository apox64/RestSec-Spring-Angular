package de.novatecgmbh.restsecspring.logic.reporting.results;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

@XmlRootElement(name = "OWASPZAPReport")
public class ZapReportDTO {

    public Collection<Sites> getSites() {
        return sites;
    }

    public void setSites(Collection<Sites> sites) {
        this.sites = sites;
    }

    public Collection<Sites> sites;

}

class Sites {

    public Collection<Alerts> getAlerts() {
        return alerts;
    }

    public void setAlerts(Collection<Alerts> alerts) {
        this.alerts = alerts;
    }

    private Collection<Alerts> alerts;

}

class Alerts {

    public Collection<AlertItemDTO> getAlertItems() {
        return alertItems;
    }

    public void setAlertItems(Collection<AlertItemDTO> alertItems) {
        this.alertItems = alertItems;
    }

    private Collection<AlertItemDTO> alertItems;

}
