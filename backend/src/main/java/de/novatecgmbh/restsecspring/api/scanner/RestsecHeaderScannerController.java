package de.novatecgmbh.restsecspring.api.scanner;

import de.novatecgmbh.restsecspring.logic.scanner.RestsecHeaderScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/scanner/restsec/header")
public class RestsecHeaderScannerController {

    private static Logger logger = LoggerFactory.getLogger(RestsecHeaderScannerController.class);

    @Autowired
    RestsecHeaderScanner restsecHeaderScanner;

    @RequestMapping(value = "start", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String startAttack() {
        logger.info("RestSec Header Scanner started.");
        restsecHeaderScanner.scanForSecurityHeaders();
        logger.info("RestSec Header Scanner finished.");
        return "{ \"status\" : \"OK\"}";
    }
}
