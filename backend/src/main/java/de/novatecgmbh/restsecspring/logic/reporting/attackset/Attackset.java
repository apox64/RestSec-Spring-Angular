package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class Attackset {

    public static Attackset instance = null;

    private static final Logger logger = LoggerFactory.getLogger(Attackset.class);
    private final JSONArray attackSetJSON = new JSONArray();

    private Attackset() {
        logger.info("New Attackset created.");
    }

    //Singleton
    public static Attackset getInstance() {
        if (instance == null) instance = new Attackset();
        return instance;
    }

    public void add(AttackableEndpoint attackableEndpoint) {
        if (!alreadyExists(attackableEndpoint)) {
            UUID id = attackableEndpoint.getId();
            if (getIndexForID(id) >= 0) {
                logger.info("Endpoint already exists with id : " + id);
                return;
            }
            logger.info("Adding \"" + id + " : " + attackableEndpoint.getEndpointURL() + "\" to Attackset.");
            JSONObject attackableEndpointJSON = new JSONObject();
            try {
                attackableEndpointJSON.put("id", id.toString());
                attackableEndpointJSON.put("httpVerb", attackableEndpoint.getHttpVerb());
                attackableEndpointJSON.put("endpointURL", attackableEndpoint.getEndpointURL());
                attackableEndpointJSON.put("scanStatus", attackableEndpoint.getScanStatus());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            attackSetJSON.put(attackableEndpointJSON);
        }
    }

    public void remove(AttackableEndpoint attackableEndpoint) {
        UUID id = attackableEndpoint.getId();
        logger.info("Removing \"" + id + "\" from Attackset.");
        attackSetJSON.remove(getIndexForID(id));
    }

    public void setScannedTrue(AttackableEndpoint attackableEndpoint) {
        UUID id = attackableEndpoint.getId();
        logger.info("Setting scanStatus = true for " + id);
        attackableEndpoint.setScanStatus(true);
    }

    public boolean getScanStatus(AttackableEndpoint attackableEndpoint) {
        return attackableEndpoint.getScanStatus();
    }

//    public AttackableEndpoint getAttackableEndpoint(UUID id) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        AttackableEndpoint attackableEndpoint = null;
//        try {
//            attackableEndpoint = objectMapper.readValue(attackSetJSON.get(getIndexForID(id)).toString(), AttackableEndpoint.class);
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//        return attackableEndpoint;
//    }

    public JSONArray getAttackSet() {
        return attackSetJSON;
    }

    private int getIndexForID(UUID id) {
        for (int i = 0; i < attackSetJSON.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) attackSetJSON.get(i);
                if (jsonObject.get("id").equals(id.toString())) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private boolean alreadyExists(AttackableEndpoint attackableEndpoint) {
        for (int i = 0; i < attackSetJSON.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) attackSetJSON.get(i);
                if (jsonObject.get("endpointURL").equals(attackableEndpoint.getEndpointURL())) {
                    logger.info("Already exists: " + attackableEndpoint.getId());
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
