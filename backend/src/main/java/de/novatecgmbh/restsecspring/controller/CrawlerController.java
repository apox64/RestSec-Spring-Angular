package de.novatecgmbh.restsecspring.controller;

import de.novatecgmbh.restsecspring.crawler.HateoasCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@RestController
public class CrawlerController {

    private static Logger logger = LoggerFactory.getLogger(CrawlerController.class);

    //TODO: Save Swagger File and then parse it.
    @RequestMapping(value = "/crawler/swagger", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public String handleFileUpload(@RequestParam ("file") MultipartFile file){
        logger.info("File upload ...");
        String name = "test1";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                bos.write(bytes);
                bos.close();
                return "Upload successful. " + name + " --> " + name + "-uploaded.";
            } catch (Exception e) {
                return "Upload failed. " + name + " : " + e.getMessage();
            }
        } else {
            return "Upload failed. File is empty.";
        }
    }

    @RequestMapping(value = "/crawler/hateoas", method = RequestMethod.POST)
    public String hateoas(@RequestParam Map<String, String> requestParams){

        String url = requestParams.get("url");
        String authtoken = requestParams.get("authtoken");

        logger.info(url);
        logger.info(authtoken);

        HateoasCrawler hateoasCrawler = new HateoasCrawler(url);
        return String.valueOf(hateoasCrawler.getNumberOfEndpoints());
    }
}
