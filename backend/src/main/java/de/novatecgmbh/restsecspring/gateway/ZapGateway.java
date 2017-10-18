package de.novatecgmbh.restsecspring.gateway;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zaproxy.clientapi.core.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        Attackset attackset = Attackset.getInstance();

        logger.info("Running Spider and Scanner for each entry (" + attackset.getAttackSet().length() + ") in current Attackset.");

        for (int i = 0; i < attackset.getAttackSet().length(); i++) {
            try {
                JSONObject attackable = attackset.getAttackSet().getJSONObject(i);
                setTargetUrl(new URL(attackable.get("endpointUrl").toString()));
                String currentSpiderScanId = runSpider();
                logger.info("spider (scanId : " + currentSpiderScanId + ") started.");
                while(getSpiderProgress(currentSpiderScanId) < 100) {
                    Thread.sleep(2000);
                }
                logger.info("spider (scanId : " + currentSpiderScanId + ") has finished.");
                String currentAscanScanId = runActiveScan();
                logger.info("ascan (scanId : " + currentAscanScanId + ") started.");
                while(getActiveScanProgress(currentAscanScanId) < 100) {
                    Thread.sleep(2000);
                }
                logger.info("ascan (scanId : " + currentAscanScanId + ") has finished.");
            } catch (JSONException | MalformedURLException | InterruptedException e) {
                e.printStackTrace();
            }
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

    public int getAverageSpiderProgress() {
        int progress = 0;
        try {
            ApiResponse apiResponse = clientApi.spider.scans();
            ApiResponseList apiResponseList = (ApiResponseList) apiResponse;
            List<ApiResponse> list = apiResponseList.getItems();

            for (int i = 0; i < list.size(); i++) {
                ApiResponseSet apiResponseSet = (ApiResponseSet) list.get(i);
                Map map = apiResponseSet.getValuesMap();
                //logger.info(map.get("progress").toString());
                progress += Integer.parseInt(map.get("progress").toString());
            }

            if (progress == 0) return 0;
            else progress /= list.size();

        } catch (ClientApiException e) {
            e.printStackTrace();
        }
        return progress;
    }

    public int getAverageScannerProgress() {
        int progress = 0;
        try {
            ApiResponse apiResponse = clientApi.ascan.scans();
            ApiResponseList apiResponseList = (ApiResponseList) apiResponse;
            List<ApiResponse> list = apiResponseList.getItems();

            for (int i = 0; i < list.size(); i++) {
                ApiResponseSet apiResponseSet = (ApiResponseSet) list.get(i);
                Map map = apiResponseSet.getValuesMap();
                //logger.info(map.get("progress").toString());
                progress += Integer.parseInt(map.get("progress").toString());
            }

            if (progress == 0) return 0;
            else progress /= list.size();

        } catch (ClientApiException e) {
            e.printStackTrace();
        }
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
            clientApi.core.newSession("", "");
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

    public void setTargetUrl(URL targetUrl) {
        this.targetUrl = targetUrl;
    }
}
