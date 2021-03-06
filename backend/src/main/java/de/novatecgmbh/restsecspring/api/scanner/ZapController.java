package de.novatecgmbh.restsecspring.api.scanner;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        String type = requestParams.get("type");
        logger.info("Status for : " + requestParams.get("type"));
        switch (type) {
            case "spider":
                return "{ \"spider\" : \"" + String.valueOf(zapGateway.getTotalSpiderProgress()) + "\" }";
            case "ascan":
                return "{ \"ascan\" : \"" + String.valueOf(zapGateway.getAverageScannerProgress()) + "\" }";
        }
        return "{\"error\" : \"unknown type\"}";
    }

    @RequestMapping(path = "start", method = RequestMethod.POST)
    public ResponseEntity zapStart(@RequestParam Map<String, String> requestParams) {
        ZapGateway zapGateway = new ZapGateway();
        zapGateway.runAll();
        return new ResponseEntity<String>("{\"status\" : \"OK\"}", new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(path = "clear", method = RequestMethod.GET)
    public ResponseEntity zapClear() {
        ZapGateway zapGateway = new ZapGateway();
        zapGateway.clearSession();
        return new ResponseEntity<String>("{\"status\" : \"OK\"}", new HttpHeaders(), HttpStatus.OK);
    }

}
