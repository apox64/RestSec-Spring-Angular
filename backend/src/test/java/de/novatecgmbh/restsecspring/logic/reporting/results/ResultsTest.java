package de.novatecgmbh.restsecspring.logic.reporting.results;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultsTest {
    @Test
    void getResultSetJSON() {
        Results.getInstance().getResultSetJSON();
    }

    @Test
    void append() {
        ZapGateway zapGateway = new ZapGateway();
        Results.getInstance().append(zapGateway.getXmlReport());
    }

    @Test
    void clearAll() {
    }

}