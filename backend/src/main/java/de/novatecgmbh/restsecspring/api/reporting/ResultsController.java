package de.novatecgmbh.restsecspring.api.reporting;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import de.novatecgmbh.restsecspring.logic.reporting.results.ResultsZap;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.IOUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@RestController
@RequestMapping(value = "/reporting/results", produces = "application/json; charset=utf-8")
public class ResultsController {

    @RequestMapping(value = "/zap/simple", method = RequestMethod.GET)
    public ResponseEntity getZapScoresSimple(HttpServletResponse response) {
        ResultsZap resultsZap = new ResultsZap();
        return new ResponseEntity<String>(resultsZap.getSimpleScores(), new HttpHeaders(), HttpStatus.OK);
    }

    @RequestMapping(path = "/zap/full", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadZapResults() throws IOException {

        ZapGateway zapGateway = new ZapGateway();
        InputStream inputStream = new ByteArrayInputStream(zapGateway.getHtmlReport().getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
//                .contentLength(htmlFile.contentLength())
                .contentLength(zapGateway.getHtmlReport().length())
                .contentType(MediaType.parseMediaType("text/html"))
//                .body(new InputStreamResource(htmlFile.getInputStream()));
                .body(new InputStreamResource(inputStream));
    }

}
