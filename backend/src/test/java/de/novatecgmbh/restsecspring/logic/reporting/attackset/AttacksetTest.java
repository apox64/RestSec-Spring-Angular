package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttacksetTest {

    private static Attackset attackset;
    private static AttackableEndpoint endpoint1;
    private static AttackableEndpoint endpoint2;

    @BeforeEach
    void setUp() {
        attackset = new Attackset();
        endpoint1 = new AttackableEndpoint();
        endpoint1.setEndpointURL("http://127.0.0.1.local/rest/1");
        endpoint1.setScanStatus(false);
        endpoint2 = new AttackableEndpoint();
        endpoint2.setEndpointURL("http://127.0.0.1.local/rest/2");
        endpoint2.setScanStatus(false);
    }

    @Test
    void add() {
        attackset.add(endpoint1);
//        assertTrue(attackset.getAttackSet().length() == 1);
    }

    @Test
    void remove() {
        attackset.add(endpoint1);
        attackset.add(endpoint2);
        attackset.remove(endpoint1);
//        assertTrue(attackset.getAttackSet().length() == 1);
    }

    @Test
    void setScanned() {
        attackset.add(endpoint1);
        attackset.setScannedTrue(endpoint1);
        assertTrue(attackset.getScanStatus(endpoint1));
    }

}