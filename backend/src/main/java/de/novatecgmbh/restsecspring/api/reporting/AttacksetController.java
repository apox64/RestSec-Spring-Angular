package de.novatecgmbh.restsecspring.api.reporting;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/reporting/attackset")
public class AttacksetController {

    private static Logger logger = LoggerFactory.getLogger(AttacksetController.class);

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String attackset() {
        Attackset attackset = Attackset.getInstance();
        AttackableEndpoint a1 = new AttackableEndpoint();
        a1.setEndpointURL("http://test.local");
        a1.setScanStatus(false);
        attackset.add(a1);
        AttackableEndpoint a2 = new AttackableEndpoint();
        a2.setEndpointURL("http://test.local/api/attack-me/");
        a2.setScanStatus(true);
        AttackableEndpoint a3 = new AttackableEndpoint();
        a3.setEndpointURL("http://test.local/test/3");
        a3.setScanStatus(false);
        attackset.add(a3);
        attackset.add(a2);
        attackset.add(a1);
        logger.info("Returning attackSet (length: " + attackset.getAttackSet().length()+")");
        return attackset.getAttackSet().toString();
    }
}
