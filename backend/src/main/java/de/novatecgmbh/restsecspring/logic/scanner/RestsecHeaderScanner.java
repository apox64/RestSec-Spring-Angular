package de.novatecgmbh.restsecspring.logic.scanner;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RestsecHeaderScanner {

    Attackset attackset = Attackset.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(RestsecHeaderScanner.class);

    private String result = "{\"riskLow\" : \"0\", \"riskMedium\" : \"0\", \"riskHigh\" : \"0\"}";

    public void scanForSecurityHeaders(){
        JSONArray attackSetJSON = attackset.getAttackSet();
        result = "{\"riskLow\" : \"test\", \"riskMedium\" : \"test\", \"riskHigh\" : \"test\"}";
    }

    public String scanSingleUrl() {
        return "something";
    }

    public String getResult() {
        return result;
    }

}
