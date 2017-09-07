package de.novatecgmbh.restsecspring.logic.crawler;

import de.novatecgmbh.restsecspring.logic.reporting.attackset.AttackableEndpoint;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwaggerCrawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);

    public SwaggerCrawler() {

    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    public void crawl(String fileLocation) {
        Swagger swagger = new SwaggerParser().read(fileLocation);
        Map<String, Path> paths = swagger.getPaths();

        for (Map.Entry<String, Path> entry : paths.entrySet()) {
            Path path = entry.getValue();
            List list = getHttpVerbsForPath(path);
            appendEndpointsToAttackset(entry.getKey(), list);
            this.numberOfEndpoints += list.size();
        }

        Attackset attackset = Attackset.getInstance();
        logger.info("AttackSet.length : " + attackset.getAttackSet().length());
    }


    //TODO: refactor methods
    private void appendEndpointsToAttackset(String path, List endpoints) {
        Attackset attackset = Attackset.getInstance();
        for (Object endpoint : endpoints) {
            attackset.add(new AttackableEndpoint(path, endpoint.toString()));
        }
    }

    private List getHttpVerbsForPath(Path path) {
        Map<HttpMethod, Operation> operationMap = path.getOperationMap();
        List<String> httpVerbs = new ArrayList<>();
        for (Map.Entry operation : operationMap.entrySet()) {
            httpVerbs.add(operation.getKey().toString());
        }
        return httpVerbs;
    }

}
