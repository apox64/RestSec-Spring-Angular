package de.novatecgmbh.restsecspring.api.scanner;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/scanner/zap", produces = "application/json; charset=UTF-8")
@CrossOrigin
public class ZapController {

    private static Logger logger = LoggerFactory.getLogger(ZapController.class);
    private String ZAP_URL = "http://localhost:8081";

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String zapOnline(@RequestParam Map<String, String> requestParams) {
        if (requestParams.get("zapUrl") != null) {
            ZAP_URL = requestParams.get("zapUrl");
        }
        logger.info("ZAP_URL set to : " + ZAP_URL);
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        if (zapGateway.isOnline(ZAP_URL)){
            return "{\"zap\" : \"online\"}";
        } else {
            return "{\"zap\" : \"offline\"}";
        }
    }

    @RequestMapping(path = "/status", method = RequestMethod.GET)
    public String zapStatus(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        logger.info("type: " + requestParams.get("type"));
        return zapGateway.getStatus(requestParams.get("type"));
    }

    @RequestMapping(path = "/start", method = RequestMethod.POST)
    public String zapStart(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway(ZAP_URL);
        logger.info("url: " + requestParams.get("url"));
        return zapGateway.startAttack(requestParams.get("url"));
    }

}
