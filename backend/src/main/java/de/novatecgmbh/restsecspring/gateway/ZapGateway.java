package de.novatecgmbh.restsecspring.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZapGateway {

    private static final Logger logger = LoggerFactory.getLogger(ZapGateway.class);

    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 8081;
    private static final String ZAP_API_KEY = null;

    private static ClientApi clientApi = null;
    private URL targetUrl = null;

    public ZapGateway() {
        clientApi = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
        if (isReachable()) {
            logger.info("ZapGateway created.");
        } else {
            logger.info("ZapGateway could not be created.");
        }
    }

    public boolean isReachable() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://" + ZAP_ADDRESS + ":" + ZAP_PORT).openConnection();
            if (connection.getResponseCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public void runAll() {
        try {
            runSpider();
            Thread.sleep(2000);
            runActiveScan();

            logger.info("Alerts:");
            logger.info(new String(clientApi.core.xmlreport()));

        } catch (Exception e) {
            logger.info("Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String runSpider() {
        logger.info("Starting Spider : " + targetUrl);
        ApiResponse resp = null;
        try {
            resp = clientApi.spider.scan(targetUrl.toString(), null, null, null, null);
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        return ((ApiResponseElement) resp).getValue();
    }

    public int getSpiderProgress(String scanId) {
        int progress = 0;
        try {
            progress = Integer.parseInt(((ApiResponseElement) clientApi.spider.status(scanId)).getValue());
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        logger.info("Spider progress : " + progress + "%");
        return progress;
    }


    public String runActiveScan() {
        logger.info("Active scan : " + targetUrl);
        ApiResponse resp = null;
        try {
            resp = clientApi.ascan.scan(targetUrl.toString(), "True", "False", null, null, null);
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        return ((ApiResponseElement) resp).getValue();
    }

    public int getActiveScanProgress(String scanId) {
        int progress = 0;
        try {
            progress = Integer.parseInt(((ApiResponseElement) clientApi.ascan.status(scanId)).getValue());
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        logger.info("Active Scan progress : " + progress + "%");
        return progress;
    }

    public void clearSession() {
        try {
            clientApi.core.newSession("","");
            ApiResponseList apiResponse = (ApiResponseList) clientApi.core.sites();
            logger.info("Session cleared. " + apiResponse.getItems().size() + " sites in new session.");
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
    }

    public String getXmlReport() {
        String report = "";
        try {
            report = new String(clientApi.core.xmlreport());
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        return report;
    }

    public String getHtmlReport() {
        String report = "";
        try {
            report = new String(clientApi.core.htmlreport());
        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        return report;
    }

    public String getStatus(String type) { return ""; }

    public void setTargetUrl(URL targetUrl) {
        this.targetUrl = targetUrl;
    }
}
