package de.novatecgmbh.restsecspring.api.scanner;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ScannerController {

    private static Logger logger = LoggerFactory.getLogger(ScannerController.class);
    private String ZAP_URL = "http://localhost:8081";

    @CrossOrigin
    @RequestMapping(value = "/scanner/zap", method = RequestMethod.GET, produces = "application/json")
    public String zapOnline(@RequestParam Map<String, String> requestParams) {
        if (requestParams.get("zapUrl") != null) {
            ZAP_URL = requestParams.get("zapUrl");
        }
        logger.info("ZAP_URL set to : " + ZAP_URL);
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        if (zapGateway.isOnline()){
            return "{\"zap\" : \"online\"}";
        } else {
            return "{\"zap\" : \"offline\"}";
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/scanner/zap/status", method = RequestMethod.GET, produces = "application/json")
    public String zapProgress(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        logger.info("type: " + requestParams.get("type"));
        return zapGateway.getStatus(requestParams.get("type"));
    }

    @CrossOrigin
    @RequestMapping(value = "/scanner/zap/start", method = RequestMethod.POST, produces = "application/json")
    public String zapStart(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        logger.info("url: " + requestParams.get("url"));
        return zapGateway.startAttack(requestParams.get("url"));
    }

}
