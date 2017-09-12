package de.novatecgmbh.restsecspring.logic.reporting.attackset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttacksetTest {

    private static Attackset attackset = Attackset.getInstance();
    private static AttackableEndpoint endpoint1;
    private static AttackableEndpoint endpoint2;

    @BeforeEach
    void setUp() {
        endpoint1 = new AttackableEndpoint("http://127.0.0.1.local/rest/1", "GET");
        endpoint1.setScanStatus(false);
        endpoint2 = new AttackableEndpoint("http://127.0.0.1.local/rest/2", "POST");
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
        attackset.removeByAttackableEndpoint(endpoint1);
//        assertTrue(attackset.getAttackSet().length() == 1);
    }

    @Test
    void setScanned() {
        attackset.add(endpoint1);
        attackset.setScannedTrue(endpoint1);
        assertTrue(attackset.getScanStatus(endpoint1));
    }

}