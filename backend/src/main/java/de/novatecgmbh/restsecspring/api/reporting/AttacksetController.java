package de.novatecgmbh.restsecspring.api.reporting;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/reporting/attackset")
public class AttacksetController {

    private static Logger logger = LoggerFactory.getLogger(AttacksetController.class);

    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Attackset attackset() {
        Attackset attackset = new Attackset();
        AttackableEndpoint a1 = new AttackableEndpoint();
        a1.setEndpointURL("http://test.com");
        a1.setScanStatus(false);
        attackset.add(a1);
        attackset.add(a1);
        attackset.add(a1);
        return attackset;
    }

}
