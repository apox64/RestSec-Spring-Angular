package de.novatecgmbh.restsecspring.logic.crawler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonJsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class SwaggerCrawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);

    private Attackset attackset;
    private File file;

    public SwaggerCrawler(File file) {
        this.file = file;
        crawlJSON(file);
//            crawl();
    }

    private void crawl() throws JSONException {
        logger.info("Crawling a file ...");
        attackset = Attackset.getInstance();
        AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
        attackableEndpoint.setEndpointURL("http://test.local");
        attackableEndpoint.setScanStatus(false);
        attackset.add(attackableEndpoint);
        // do some file magic here ...
//        this.numberOfEndpoints = getNumberOfEndpoints();
    }


    //TODO: Continue here
    private void crawlJSON(File file) {

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        Map<String, Object> map = null;

        try {
            json = new Scanner(file).useDelimiter("\\Z").next();
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject(map);
        JSONObject pathsObject = new JSONObject();
        try {
            pathsObject = (JSONObject) jsonObject.get("paths");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Attackset attackset = Attackset.getInstance();

        Iterator<String> it = pathsObject.keys();
        int attackCounter = 0;

        while (it.hasNext()) {
            String currentPath = it.next();
//            logger.info("Checking path: \t\t" + currentPath);
            JSONObject httpVerbsObject = null;
            try {
                httpVerbsObject = (JSONObject) pathsObject.get(currentPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = new JSONArray();

            Iterator<String> iterator = httpVerbsObject.keys();
            while (iterator.hasNext()) {
                String s = iterator.next();
                switch (s) {
                    case "get":
                        AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
                        attackableEndpoint.setHttpVerb("GET");
                        attackableEndpoint.setEndpointURL(currentPath);
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                    case "post":
                        AttackableEndpoint attackableEndpoint2 = new AttackableEndpoint();
                        attackableEndpoint2.setHttpVerb("POST");
                        attackableEndpoint2.setEndpointURL(currentPath);
                        attackset.add(attackableEndpoint2);
                        attackCounter++;
                    case "patch":
                        AttackableEndpoint attackableEndpoint3 = new AttackableEndpoint();
                        attackableEndpoint3.setHttpVerb("PATCH");
                        attackableEndpoint3.setEndpointURL(currentPath);
                        attackset.add(attackableEndpoint3);
                        attackCounter++;
                    case "put":
                        AttackableEndpoint attackableEndpoint4 = new AttackableEndpoint();
                        attackableEndpoint4.setHttpVerb("PUT");
                        attackableEndpoint4.setEndpointURL(currentPath);
                        attackset.add(attackableEndpoint4);
                        attackCounter++;
                    case "delete":
                        AttackableEndpoint attackableEndpoint5 = new AttackableEndpoint();
                        attackableEndpoint5.setHttpVerb("DELETE");
                        attackableEndpoint5.setEndpointURL(currentPath);
                        attackset.add(attackableEndpoint5);
                        attackCounter++;
                }
            }

        }
        logger.info("" + attackCounter + " attackable endpoints found.");
        logger.info("AttackSet counter = " + attackset.getAttackSet().length());
    }

}
