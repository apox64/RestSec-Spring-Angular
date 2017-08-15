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

    public SwaggerCrawler() {

    }

    public void crawlJSON(File file) {

        ObjectMapper mapper = new ObjectMapper();
        String json;
        Map<String, Object> map = null;

        try {
            json = new Scanner(file).useDelimiter("\\Z").next();
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
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
            JSONObject httpVerbsObject = null;
            try {
                httpVerbsObject = (JSONObject) pathsObject.get(currentPath);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Iterator<String> iterator = httpVerbsObject.keys();
            while (iterator.hasNext()) {
                AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
                attackableEndpoint.setEndpointURL(currentPath);
                String s = iterator.next();
                switch (s) {
                    case "get":
                        attackableEndpoint.setHttpVerb("GET");
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                        break;
                    case "post":
                        attackableEndpoint.setHttpVerb("POST");
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                        break;
                    case "patch":
                        attackableEndpoint.setHttpVerb("PATCH");
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                        break;
                    case "put":
                        attackableEndpoint.setHttpVerb("PUT");
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                        break;
                    case "delete":
                        attackableEndpoint.setHttpVerb("DELETE");
                        attackset.add(attackableEndpoint);
                        attackCounter++;
                        break;
                }
                attackset.add(attackableEndpoint);
            }

        }
        logger.info("" + attackCounter + " attackable endpoints found.");
        this.numberOfEndpoints = attackCounter;
        logger.info("AttackSet.length : " + attackset.getAttackSet().length());
    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }
}
