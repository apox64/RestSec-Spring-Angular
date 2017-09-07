package de.novatecgmbh.restsecspring.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

@Component
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private final String SERVER_ADDRESS = "";
    private final String TCP_SERVER_PORT = "";

    private HttpUtils() {
    }

    public boolean isOnline(String url) {
        try {
            new URL(url).openStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (Socket s = new Socket(url, 80)) {
            return true;
        } catch (IOException ex) {
                /* ignore */
        }
        return false;
    }

    public boolean isOnline(String hostname, int port) {
        try (Socket s = new Socket(hostname, port)) {
            return true;
        } catch (IOException ex) {
                /* ignore */
        }
        return false;
    }

}
