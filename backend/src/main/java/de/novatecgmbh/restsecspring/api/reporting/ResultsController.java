package de.novatecgmbh.restsecspring.api.reporting;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
@RequestMapping(value = "/reporting/results")
public class ResultsController {

    @RequestMapping(value = "/zap", method = RequestMethod.GET)
    public void getZapReportHtml(HttpServletResponse response) {
        try {
            ZapGateway zapGateway = new ZapGateway();
            String htmlReport = zapGateway.getHtmlReport();

            String fileName = "test.txt";

            //response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);

//            String source = "This is the source of my input stream";
//            InputStream in = IOUtils.toInputStream(source, "UTF-8");

            InputStream inputStream = new ByteArrayInputStream(htmlReport.getBytes(StandardCharsets.UTF_8.name()));
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(path = "/zap/2", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadPDFFile()
            throws IOException {

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
                .contentType(MediaType.parseMediaType("application/octet-stream"))
//                .body(new InputStreamResource(htmlFile.getInputStream()));
                .body(new InputStreamResource(inputStream));
    }

}
