package de.novatecgmbh.restsecspring.api.crawler;

import de.novatecgmbh.restsecspring.logic.crawler.SwaggerCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/crawler/swagger")
public class SwaggerCrawlerController {

    private static Logger logger = LoggerFactory.getLogger(SwaggerCrawlerController.class);

    //TODO: Save Swagger File and then parse it.
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
                SwaggerCrawler swaggerCrawler = new SwaggerCrawler(new File("src/main/resources/uploads/"+fileName));
                return "Upload successful. " + fileName + " --> " + fileName;
            } catch (Exception e) {
                return "Upload failed. " + fileName + " : " + e.getMessage();
            }
        } else {
            return "Upload failed. File is empty.";
        }
    }

    public File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException
    {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }


}
