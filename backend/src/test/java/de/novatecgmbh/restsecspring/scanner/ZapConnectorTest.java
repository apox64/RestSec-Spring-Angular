package de.novatecgmbh.restsecspring.scanner;

import org.junit.jupiter.api.Test;

class ZapConnectorTest {

    private String ZAP_URL = "http://localhost:8081";

    @Test
    void getStatus() {
        ZapConnector zapConnector = new ZapConnector(ZAP_URL);
        System.out.println(zapConnector.getStatus("spider"));
        System.out.println(zapConnector.getStatus("ascan"));
    }

    @Test
    void startAttack() {
        ZapConnector zapConnector = new ZapConnector(ZAP_URL);
        System.out.println(zapConnector.startAttack("http://127.0.0.1:8080"));
    }

    //TODO: Get number of alerts

    //TODO: clear session

    //TODO: add recurse button

}