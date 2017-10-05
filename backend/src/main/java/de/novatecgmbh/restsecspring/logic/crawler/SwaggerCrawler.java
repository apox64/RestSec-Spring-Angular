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

public class SwaggerCrawler implements Crawler {

    private int numberOfEndpoints = 0;
    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawler.class);
    private static String hostAndBasePath = "";

    public SwaggerCrawler() {

    }

    public int getNumberOfEndpoints() {
        return numberOfEndpoints;
    }

    @Override
    public void crawl(String fileLocation) {
        Swagger swagger = new SwaggerParser().read(fileLocation);
        determineHostAndBasePath(swagger);
        Map<String, Path> paths = swagger.getPaths();
        for (Map.Entry<String, Path> path : paths.entrySet()) {
            List<String> endpoints = getHttpVerbsForPath(path.getValue());
            appendEndpointsToAttackset(path.getKey(), endpoints);
            this.numberOfEndpoints += endpoints.size();
        }
    }

    private void determineHostAndBasePath(Swagger swagger) {
        if (swagger.getBasePath().equals("/")) {
            hostAndBasePath = swagger.getHost();
        } else {
            hostAndBasePath = swagger.getHost() + swagger.getBasePath();
        }
    }

    private void appendEndpointsToAttackset(String path, List<String> endpoints) {
        endpoints.forEach(endpoint -> Attackset.getInstance().add(new AttackableEndpoint(hostAndBasePath + path, endpoint)));
    }

    private List<String> getHttpVerbsForPath(Path path) {
        Map<HttpMethod, Operation> operationMap = path.getOperationMap();
        List<String> httpVerbs = new ArrayList<>();
        operationMap.forEach((httpMethod, operation) -> httpVerbs.add(httpMethod.name()));
        return httpVerbs;
    }
}
