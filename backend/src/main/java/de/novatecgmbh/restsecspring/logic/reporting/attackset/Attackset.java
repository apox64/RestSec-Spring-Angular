package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Attackset {

    private static final Logger logger = LoggerFactory.getLogger(Attackset.class);
    private final HashMap<Integer, AttackableEndpoint> attackSet;

    public Attackset() {
        attackSet = new HashMap<>();
        logger.info("New Attackset created.");
    }

    public void add(AttackableEndpoint attackableEndpoint) {
        int currentMaxID = getCurrentMaxID();
        logger.info("Adding \"" + (currentMaxID+1) + ":" + attackableEndpoint.getEndpointURL() + "\" to Attackset.");
        attackableEndpoint.setScanStatus(false);
        attackSet.put(currentMaxID+1, attackableEndpoint);
    }

    public void add(int id, AttackableEndpoint attackableEndpoint) {
        logger.info("Adding \"" + id + ":" + attackableEndpoint.getEndpointURL() + "\" to Attackset.");
        attackableEndpoint.setScanStatus(false);
        attackSet.put(id, attackableEndpoint);
    }

    public void remove(int id) {
        logger.info("Removing " + id + " from Attackset.");
        attackSet.remove(id);
    }

    public void setScanned(int id) {
        logger.info("Setting scanStatus = true for " + id);
        AttackableEndpoint attackableEndpoint = attackSet.get(id);
        attackableEndpoint.setScanStatus(true);
    }

    public boolean getScanned(int id) {
        logger.info("Getting scanStatus for " + id);
        AttackableEndpoint attackableEndpoint = attackSet.get(id);
        return attackableEndpoint.getScanStatus();
    }

    public AttackableEndpoint get(int id) {
        return attackSet.get(id);
    }

    public HashMap getHashMap() {
        return attackSet;
    }

    private int getCurrentMaxID() {
        if (attackSet.size() == 0) {
            return 0;
        }
        int maxID = Collections.max(attackSet.entrySet(), Map.Entry.comparingByKey()).getKey();
        return maxID;
    }

}
