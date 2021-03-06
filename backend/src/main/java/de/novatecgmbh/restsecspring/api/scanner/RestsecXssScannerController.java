package de.novatecgmbh.restsecspring.api.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/scanner/restsec/xss")
public class RestsecXssScannerController {

    private static Logger logger = LoggerFactory.getLogger(RestsecXssScannerController.class);

    @RequestMapping(value = "start", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String startAttack(String requestBody) {
        logger.info("RestSec XSS Scanner started.");
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("RestSec XSS Scanner finished.");
        return "{ \"status\" : \"OK\"}";
    }
}
