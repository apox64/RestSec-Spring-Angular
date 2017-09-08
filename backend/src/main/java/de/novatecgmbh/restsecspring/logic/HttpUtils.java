package de.novatecgmbh.restsecspring.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.URL;

@Component
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public HttpUtils() {
    }

    public boolean isOnline(String url) {
        logger.info("Connecting to : \"" + url + "\"");
        try {
            new URL(url).openStream().close();
        } catch (ConnectException e) {
            logger.info("Could not connect.");
            return false;
        } catch (Exception e) {
            logger.info("Malformed URL.");
            return false;
        }
        return true;
    }

}
