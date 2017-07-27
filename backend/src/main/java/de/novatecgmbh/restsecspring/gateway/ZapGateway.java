package de.novatecgmbh.restsecspring.gateway;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZapGateway {

    //TODO: use zap-clientapi 1.3.0

    private static Logger logger = LoggerFactory.getLogger(ZapGateway.class);
    private static String ZAP_URL = "http://127.0.0.1:8081";

    public ZapGateway(String url) {
        ZapGateway.ZAP_URL = url;
    }

    public boolean isOnline() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(ZAP_URL).openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                logger.info("Zap is online.");
                return true;
            }
        } catch (IOException e) {
            logger.info("Zap is offline.");
            return false;
        }
        return false;
    }

    @JsonSerialize(using = DateSerializer.class)
    public String getStatus(String type) {
        RestTemplate restTemplate = new RestTemplate();
        String spiderStatusURL = ZAP_URL + "/JSON/" + type + "/view/scans/?zapapiformat=JSON&formMethod=GET";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(restTemplate.getForObject(spiderStatusURL, String.class));
            jsonObject.put(type, jsonObject.remove("scans"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        logger.info(jsonObject.toString());

        return jsonObject.toString();
    }

    public String startAttack(String TARGET_URL) {
        RestTemplate restTemplate = new RestTemplate();

        logger.info("TARGET_URL: " + TARGET_URL);

        // Starting Spider
        String spiderStartURL = ZAP_URL + "/JSON/spider/action/scan/?zapapiformat=JSON&formMethod=GET&url=" + TARGET_URL + "&maxChildren=&recurse=&contextName=&subtreeOnly=";
        restTemplate.getForObject(spiderStartURL, String.class);
        if (!getStatus("spider").equals("{\"spider\":[]}")) {
            logger.info("Spider started.");
        } else {
            logger.info("Spider not started.");
        }

        while (!hasSpiderFinished()) {
            logger.info("Spider is still running ...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        logger.info("Spider finished.");

        // Starting Active Scan
        String scanStartURL = ZAP_URL + "/JSON/ascan/action/scan/?zapapiformat=JSON&formMethod=GET&url=" + TARGET_URL + "&maxChildren=&recurse=&contextName=&subtreeOnly=";
        restTemplate.getForObject(scanStartURL, String.class);
        if (!getStatus("ascan").equals("{\"ascan\":[]}")) {
            logger.info("Scanner started.");
        } else {
            logger.info("Scanner not started.");
        }

        while (!hasScannerFinished()) {
            logger.info("Scanner is still running ...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        JSONObject status = new JSONObject();

        try {
            status.put("spider", "finished");
            status.put("scanner", "finished");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return status.toString();

    }

    private boolean hasSpiderFinished() {
        return !getStatus("spider").contains("RUNNING");
    }

    private boolean hasScannerFinished() {
        return !getStatus("ascan").contains("RUNNING");
    }
}
