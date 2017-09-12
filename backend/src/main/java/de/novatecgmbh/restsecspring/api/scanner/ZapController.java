package de.novatecgmbh.restsecspring.api.scanner;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@RestController
@RequestMapping(value = "/scanner/zap", produces = "application/json; charset=UTF-8")
@CrossOrigin
public class ZapController {

    private static Logger logger = LoggerFactory.getLogger(ZapController.class);

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String zapOnline() {
        ZapGateway zapGateway = new ZapGateway();
        if (zapGateway.isReachable()) {
            return "{\"zap\" : \"online\"}";
        } else {
            return "{\"zap\" : \"offline\"}";
        }
    }

    @RequestMapping(path = "status", method = RequestMethod.GET)
    public String zapStatus(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway();
        logger.info("type: " + requestParams.get("type"));
        return zapGateway.getStatus(requestParams.get("type"));
    }

    @RequestMapping(path = "start", method = RequestMethod.POST)
    public String zapStart(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway();
        URL targetUrl = null;
        try {
            targetUrl = new URL(requestParams.get("targetUrl"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        logger.info("targetUrl: " + targetUrl.toString());
        zapGateway.setTargetUrl(targetUrl);
        zapGateway.runAll();
        return "not yet implemented";
    }

}
