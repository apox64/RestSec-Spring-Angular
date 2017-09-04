package de.novatecgmbh.restsecspring.api.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/scanner/restsec/header")
public class RestsecHeaderScannerController {

    private static Logger logger = LoggerFactory.getLogger(RestsecHeaderScannerController.class);

    @RequestMapping(value = "start", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String startAttack(String requestBody) {
        logger.info("RestSec Header Scanner started.");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("RestSec Header Scanner finished.");
        return "{ \"status\" : \"OK\"}";
    }
}
