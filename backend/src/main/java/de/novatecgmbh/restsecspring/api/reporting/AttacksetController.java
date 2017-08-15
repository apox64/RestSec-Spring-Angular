package de.novatecgmbh.restsecspring.api.reporting;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/reporting/attackset")
public class AttacksetController {

    private static Logger logger = LoggerFactory.getLogger(AttacksetController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String attackset() {
        Attackset attackset = Attackset.getInstance();
        logger.info("Returning current Attackset ...");
        return String.valueOf(attackset.getAttackSet());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public String deleteAttackableEndpoint(@PathVariable("id") UUID id) {
        Attackset attackset = Attackset.getInstance();
        if (attackset.contains(id)) {
            attackset.remove(id);
            return "{ \"status\" : \"OK\"}";
        }
        return "{ \"status\" : \"Failed\"}";
    }
}
