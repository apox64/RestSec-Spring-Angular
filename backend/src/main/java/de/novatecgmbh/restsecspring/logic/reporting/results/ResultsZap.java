package de.novatecgmbh.restsecspring.logic.reporting.results;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ResultsZap {

    private static Logger logger = LoggerFactory.getLogger(ResultsZap.class);
    private ZapGateway zapGateway = new ZapGateway();

    private int riskLow = 0;
    private int riskMedium = 0;
    private int riskHigh = 0;

    public String getSimpleScores() {
        String rawXml = zapGateway.getXmlReport();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new ByteArrayInputStream(rawXml.getBytes("utf-8"))));
            document.getDocumentElement().normalize();
            Element rootElement = document.getDocumentElement();
            NodeList siteList = document.getElementsByTagName("site");
            getAlertsForSiteNodeList(siteList);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return "{\"riskLow\" : \"" + riskLow + "\", \"riskMedium\" : \"" + riskMedium + "\", \"riskHigh\" : \"" + riskHigh + "\"}";
    }

    private void getAlertsForSiteNodeList(NodeList siteList) {
        for (int i = 0; i < siteList.getLength(); i++) {
            Node site = siteList.item(i);
            Element siteElement = (Element) site;
            NodeList alertsList = ((Element) site).getElementsByTagName("alerts");
            getAlertItemsForAlertsNodeList(alertsList);
        }
    }

    private void getAlertItemsForAlertsNodeList(NodeList alertsList) {
        for (int i = 0; i < alertsList.getLength(); i++) {
            Node alerts = alertsList.item(i);
            Element alertsElement = (Element) alerts;
            NodeList alertitemList = ((Element) alerts).getElementsByTagName("alertitem");

            for (int j = 0; j < alertitemList.getLength(); j++) {
                Node alertitem = alertitemList.item(j);
                Element alertitemElement = (Element) alertitem;
                int riskCode = Integer.parseInt(alertitemElement.getElementsByTagName("riskcode").item(0).getTextContent());
                if (riskCode == 1) riskLow++;
                if (riskCode == 2) riskMedium++;
                if (riskCode == 3) riskHigh++;
            }

            logger.info(alertitemList.getLength() + " risk scores added.");

        }
    }

}
