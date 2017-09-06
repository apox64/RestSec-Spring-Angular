package de.novatecgmbh.restsecspring.api.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/scanner/sqlmap")
public class SqlmapController {

    private static Logger logger = LoggerFactory.getLogger(SqlmapController.class);

    @RequestMapping(value = "start", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String startAttack(String requestBody) {
        logger.info("sqlmap started.");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("sqlmap finished.");
        return "{ \"status\" : \"OK\"}";
    }
}
