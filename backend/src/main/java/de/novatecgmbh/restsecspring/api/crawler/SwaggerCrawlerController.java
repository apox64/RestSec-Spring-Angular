package de.novatecgmbh.restsecspring.api.crawler;

import de.novatecgmbh.restsecspring.logic.crawler.SwaggerCrawler;
import de.novatecgmbh.restsecspring.logic.reporting.attackset.Attackset;
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
    @RequestMapping(value = "/upload", method = RequestMethod.POST)//, headers = "content-type=multipart/form-data")
    public String handleFileUpload(@RequestParam ("uploadFile") MultipartFile multipartFile){
        logger.info("File received! Name: " + multipartFile.getOriginalFilename() + " Size: " + multipartFile.getSize());
        String fileName = multipartFile.getOriginalFilename();

        Pattern jsonPattern = Pattern.compile(".*json$");
        Pattern yamlPattern = Pattern.compile(".ya?ml$");
        Matcher jsonMatcher = jsonPattern.matcher(fileName);
        Matcher yamlMatcher = yamlPattern.matcher(fileName);

        if (jsonMatcher.find()) {
            logger.info("Swagger JSON ...");
        } else if (yamlMatcher.find()) {
            logger.info("Swagger YAML ...");
        } else {
            logger.warn("Unknown Filetype.");
        }

        if (!multipartFile.isEmpty()) {
            try {
                byte[] bytes = multipartFile.getBytes();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/uploads/"+fileName)));
                bos.write(bytes);
                bos.close();
                SwaggerCrawler swaggerCrawler = new SwaggerCrawler();
                swaggerCrawler.crawlJSON(new File("src/main/resources/uploads/"+fileName));
                logger.info("Sending: " + String.valueOf(swaggerCrawler.getNumberOfEndpoints()));
//                Attackset attackset = Attackset.getInstance();
//                return attackset.getAttackSet().toString();
                return String.valueOf(swaggerCrawler.getNumberOfEndpoints());
            } catch (Exception e) {
                return "Upload failed. " + fileName + " : " + e.getMessage();
            }
        } else {
            return "Upload failed. File is empty.";
        }
    }

}
