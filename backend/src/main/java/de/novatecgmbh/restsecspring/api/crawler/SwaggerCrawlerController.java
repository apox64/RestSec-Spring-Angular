package de.novatecgmbh.restsecspring.api.crawler;

import de.novatecgmbh.restsecspring.logic.crawler.SwaggerCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/crawler/swagger")
public class SwaggerCrawlerController {

    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawlerController.class);

    @CrossOrigin
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam ("uploadFile") MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        logger.info("File received : " + fileName + " (size: " + multipartFile.getSize() + ")");

        // accepts .json, .yaml, .yml, filename has to be at least one char before the "."
        Pattern allowedFiles = Pattern.compile("([^\\s]+(\\.(?i)(json|ya?ml))$)");
        Matcher matcher = allowedFiles.matcher(fileName);

        if (!matcher.find()) {
            logger.warn("Unknown Filetype.");
            return "Upload failed. Unknown Filetype.";
        }

        if (!multipartFile.isEmpty()) {
            try {
                byte[] bytes = multipartFile.getBytes();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/uploads/"+fileName)));
                bos.write(bytes);
                bos.close();
                SwaggerCrawler swaggerCrawler = new SwaggerCrawler();
                swaggerCrawler.crawl("src/main/resources/uploads/"+fileName);
                return "{\"numberOfEndpointsFound\":\"" + String.valueOf(swaggerCrawler.getNumberOfEndpoints() + "\"}");
            } catch (Exception e) {
                return "Upload failed. " + fileName + " : " + e.getMessage();
            }
        } else {
            return "Upload failed. File is empty.";
        }
    }

}
