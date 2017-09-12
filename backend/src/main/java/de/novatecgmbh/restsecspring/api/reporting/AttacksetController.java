package de.novatecgmbh.restsecspring.api.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/reporting/attackset")
public class AttacksetController {

    private static Logger logger = LoggerFactory.getLogger(AttacksetController.class);

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String attackset() {
        Attackset attackset = Attackset.getInstance();
        logger.info("Returning current Attackset ...");
        return String.valueOf(attackset.getAttackSet());
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=utf-8", consumes = "application/json")
    @ResponseBody
    public String addAttackableEndpoint(@RequestBody String requestBody) {
        try {
            AttackableEndpoint attackableEndpoint = new ObjectMapper().readValue(requestBody, AttackableEndpoint.class);
            Attackset attackset = Attackset.getInstance();
            attackset.add(attackableEndpoint);
            return "{ \"status\" : \"OK\"}";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{ \"status\" : \"Failed\"}";
    }

    @RequestMapping(value = "", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String deleteAll() {
        Attackset attackset = Attackset.getInstance();
        logger.info("Deleting current Attackset ...");
        attackset.removeAll();
        if (attackset.getAttackSet().length() == 0) {
            return "{ \"status\" : \"OK\"}";
        }
        return "{ \"status\" : \"Failed\"}";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public String deleteAttackableEndpoint(@PathVariable("id") String strId) {
        UUID id;
        try {
            id = UUID.fromString(strId);
        } catch (Exception e) {
            logger.info("Given String was not of valid UUID form.");
            return "{ \"status\" : \"Failed\"}";
        }

        Attackset attackset = Attackset.getInstance();
        if (attackset.containsId(id)) {
            attackset.removeById(id);
            return "{ \"status\" : \"OK\"}";
        }
        return "{ \"status\" : \"Failed\"}";
    }
}
