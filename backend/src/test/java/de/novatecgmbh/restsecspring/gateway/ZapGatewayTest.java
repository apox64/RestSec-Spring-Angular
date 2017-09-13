package de.novatecgmbh.restsecspring.gateway;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

class ZapGatewayTest {

    private ZapGateway zapGateway;

    @BeforeEach
    void setUp() {
        zapGateway = new ZapGateway();
        URL targetUrl = null;
        try {
            targetUrl = new URL("http://192.168.99.100:32768/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        zapGateway.setTargetUrl(targetUrl);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void run() {
        zapGateway.runAll();
    }

    @Test
    void runSpider() {
        String scanID = zapGateway.runSpider();
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (zapGateway.getSpiderProgress(scanID) >= 100) break;
        }
    }

    @Test
    void runActiveScan() {
        String scanID = zapGateway.runActiveScan();
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (zapGateway.getActiveScanProgress(scanID) >= 100) break;
        }
    }

    @Test
    void clearSession() {
        zapGateway.clearSession();
    }

    @Test
    void getXmlReport() {
        System.out.println("------------------------------------------------------");
        System.out.println(zapGateway.getXmlReport());
        System.out.println("------------------------------------------------------");
    }

    //TODO: clear session

    //TODO: add recurse button

}