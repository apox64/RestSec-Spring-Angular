package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class Attackset {

    private static Attackset instance = null;

    private static final Logger logger = LoggerFactory.getLogger(Attackset.class);
    private JSONArray attackSetJSON = new JSONArray();

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
                return;
            }
            logger.info("+ " + id + " : " + attackableEndpoint.getEndpointURL() + " : " + attackableEndpoint.getHttpVerb());
            JSONObject attackableEndpointJSON = new JSONObject();
            try {
                attackableEndpointJSON.put("id", id.toString());
                attackableEndpointJSON.put("httpVerb", attackableEndpoint.getHttpVerb());
                attackableEndpointJSON.put("endpointUrl", attackableEndpoint.getEndpointURL());
                attackableEndpointJSON.put("scanStatus", attackableEndpoint.getScanStatus());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            attackSetJSON.put(attackableEndpointJSON);
        }
    }

    public void removeById(UUID attackableEndpointID) {
        logger.info("- " + attackableEndpointID + "");
        attackSetJSON.remove(getIndexForID(attackableEndpointID));
    }

    public void removeAll() {
        logger.info("- Attackset (complete)");
        attackSetJSON = new JSONArray();
    }

    public JSONArray getAttackSet() {
        return attackSetJSON;
    }

    public boolean containsId(UUID attackableEndpointID) {
        try {
            getAttackSet().get(getIndexForID(attackableEndpointID));
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    void removeByAttackableEndpoint(AttackableEndpoint attackableEndpoint) {
        UUID id = attackableEndpoint.getId();
        logger.info("- " + id + " : " + attackableEndpoint.getEndpointURL() + " : " + attackableEndpoint.getHttpVerb());
        attackSetJSON.remove(getIndexForID(id));
    }

    void setScannedTrue(AttackableEndpoint attackableEndpoint) {
        UUID id = attackableEndpoint.getId();
        logger.info("Setting scanStatus = true for " + id);
        attackableEndpoint.setScanStatus(true);
    }

    boolean getScanStatus(AttackableEndpoint attackableEndpoint) {
        return attackableEndpoint.getScanStatus();
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
                if (jsonObject.get("endpointUrl").equals(attackableEndpoint.getEndpointURL()) &&
                        jsonObject.get("httpVerb").equals(attackableEndpoint.getHttpVerb())) {
//                    logger.info("Already exists: " + attackableEndpoint.getId());
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
