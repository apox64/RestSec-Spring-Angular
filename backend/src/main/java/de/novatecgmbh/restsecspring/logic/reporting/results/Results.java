package de.novatecgmbh.restsecspring.logic.reporting.results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class Results {

    private static final Logger logger = LoggerFactory.getLogger(Results.class);
    private static Results instance = null;
    private JSONArray resultSetJSON = new JSONArray();
    private Document resultSet = null;

    private Results() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        String init = "<?xml version=\"1.0\"?><OWASPZAPReport><site host=\"mysite\"></site></OWASPZAPReport>";
        try {
            builder = factory.newDocumentBuilder();
            resultSet = builder.parse(new InputSource(new StringReader(init)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("New Results created.");
    }

    //Singleton
    public static Results getInstance() {
        if (instance == null) instance = new Results();
        return instance;
    }

    public AlertItemDTO xmlStringToAlertItem(String xmlString) {
        AlertItemDTO alert = new AlertItemDTO();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AlertItemDTO.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlString);
            alert = (AlertItemDTO) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return alert;
    }

    public void append(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document appendix = builder.parse(new InputSource(new StringReader(xmlString)));
            if (appendix.getDocumentElement().getNodeName().equals("OWASPZAPReport")) {
                logger.info("Appending new OWASP ZAP Report ...");
                NodeList siteList = appendix.getElementsByTagName("site");
                logger.info("siteList.length: " + siteList.getLength());

                //TODO: UNCHECKED CODE
                Element root = appendix.getDocumentElement();

                Collection<ResultEntry> resultEntries = new ArrayList<>();
                resultEntries.add(new ResultEntry());

                for (ResultEntry resultEntry : resultEntries) {
                    // resultEntry elements
                    Element newResultEntry = appendix.createElement("resultEntry");

                    Element name = appendix.createElement("name");
                    name.appendChild(appendix.createTextNode(resultEntry.getName()));
                    newResultEntry.appendChild(name);

                    Element port = appendix.createElement("port");
                    port.appendChild(appendix.createTextNode(Integer.toString(resultEntry.getScore())));
                    newResultEntry.appendChild(port);

                    root.appendChild(newResultEntry);
                }

                DOMSource source = new DOMSource(appendix);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                StreamResult result = new StreamResult("server.xml");
                transformer.transform(source, result);




//                for (int i = 0; i < siteList.getLength(); i++){
//                    Node site = siteList.item(i);
//                    Node copySite = resultSet.importNode(site, true);
//                    root.appendChild(copySite);
                    //NodeList childList = siteList.item(i).getChildNodes();
                    //NamedNodeMap namedNodeMap = siteList.item(i).getAttributes();
                    //logger.info(namedNodeMap.getNamedItem("name").toString());
                    //resultSet.appendChild(siteList.item(i));
                    //resultSet.appendChild(site);
//                }
                System.out.println("\n\n\n\n\n-------------\n\n\n\n\n");
                printDocument(resultSet, System.out);

//                printDocument(appendix, System.out);
//                //NodeList nodes = resultSetJSON.getElementsByTagName("OWASPZAPReport").item(0).getChildNodes();
//                NodeList alertsList = appendix.getElementsByTagName("alerts");
//                for (int i = 0; i < alertsList.getLength(); i++ ){
//                    Node node = alertsList.item(i);
//                    resultSet.appendChild(node);
//                }
            } else {
                logger.info("Appending something else ...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not parse xmlString.");
        }
    }

    public void clearAll() {
        this.resultSet = null;
    }

    public JSONArray getResultSetJSON() {
        System.out.println("\n\n\n-----\n\n\n");
        printDocument(resultSet, System.out);
        //Filter out the "most important" Elements from Document resultSet to JSON
        NodeList siteList = resultSet.getElementsByTagName("site");
        for (int i = 0; i < siteList.getLength(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("numberOfSites", siteList.getLength());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultSetJSON.put(jsonObject);
        }
        logger.info(resultSetJSON.toString());
        return resultSetJSON;
    }

    private void removeDuplicates(Document document) {
        System.out.println("Root element : " + document.getDocumentElement().getNodeName());
        System.out.println("----------------------------");
        NodeList alertsList = document.getElementsByTagName("alerts").item(0).getChildNodes();
        logger.info("alertsList.length : " + alertsList.getLength());
        for (int i = 0; i < alertsList.getLength(); i++) {
            NodeList childList = alertsList.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("alert".equals(childNode.getNodeName()))
                    logger.info("ALERT: " + childList.item(j).getTextContent().trim());
            }
        }
    }

    private static void printDocument(Document doc, OutputStream out) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        try {
            transformer.transform(new DOMSource(doc),
                    new StreamResult(new OutputStreamWriter(out, "UTF-8")));
        } catch (TransformerException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //TODO: get number of alerts of different levels from zap xml file

    //TODO: offer download of html reports

    public JSONObject getZAPStatistics() {

        JSONObject jsonObject = new JSONObject();
        JSONObject alertFrequencies = new JSONObject();
        try {
            alertFrequencies.put("high", "0");
            alertFrequencies.put("medium", "0");
            alertFrequencies.put("low", "0");
            jsonObject.put("OWASPZAPReport", alertFrequencies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    public ZapReportDTO getZAPReport() {
        ZapReportDTO zapReportDTO = new ZapReportDTO();
        try {
            File fXmlFile = new File("src/test/resources/sample-report-owaspzapproxy.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            JAXBContext jaxbContext = JAXBContext.newInstance(ZapReportDTO.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(doc.toString());
            zapReportDTO = (ZapReportDTO) unmarshaller.unmarshal(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zapReportDTO;
    }

    public static class ResultEntry {
        String getName() { return "http://mysite/rest/api/user"; }
        Integer getScore() { return 3; }
    }

}
