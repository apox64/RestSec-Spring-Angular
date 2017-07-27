package de.novatecgmbh.restsecspring.scanner;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.junit.jupiter.api.Test;

class ZapGatewayTest {

    private String ZAP_URL = "http://localhost:8081";

    @Test
    void getStatus() {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        System.out.println(zapGateway.getStatus("spider"));
        System.out.println(zapGateway.getStatus("ascan"));
    }

    @Test
    void startAttack() {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        System.out.println(zapGateway.startAttack("http://127.0.0.1:8080"));
    }

    //TODO: Get number of alerts

    //TODO: clear session

    //TODO: add recurse button

}