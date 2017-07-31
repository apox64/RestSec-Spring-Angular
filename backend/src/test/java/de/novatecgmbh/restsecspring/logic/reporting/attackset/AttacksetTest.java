package de.novatecgmbh.restsecspring.logic.reporting.attackset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttacksetTest {

    private static Attackset attackset = new Attackset();

    @Test
    void add() {
        AttackableEndpoint attackableEndpoint = new AttackableEndpoint();
        attackableEndpoint.setEndpointURL("http://127.0.0.1.local");
        attackset.add(1, attackableEndpoint);
        assertTrue(attackset.getHashMap().containsKey(1));
    }

    @Test
    void remove() {
        attackset.remove(1);
        assertTrue(attackset.getHashMap().isEmpty());
    }

    @Test
    void setScanned() {
        add();
        attackset.setScanned(1);
        assertTrue(attackset.getScanned(1));
    }

}