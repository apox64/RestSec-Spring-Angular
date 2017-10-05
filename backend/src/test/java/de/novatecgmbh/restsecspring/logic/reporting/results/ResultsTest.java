package de.novatecgmbh.restsecspring.logic.reporting.results;

import de.novatecgmbh.restsecspring.gateway.ZapGateway;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class ResultsTest {
    @Test
    void getZAPStatistics() {
        ZapReportDTO zapReportDTO = Results.getInstance().getZAPReport();
    }

    @Test
    void xmlStringToAlertItem() {
        String xmlString = "<alertitem>\n" +
                "<pluginid>10016</pluginid>\n" +
                "<alert>Web Browser XSS Protection Not Enabled</alert>\n" +
                "<name>Web Browser XSS Protection Not Enabled</name>\n" +
                "<riskcode>1</riskcode>\n" +
                "<confidence>2</confidence>\n" +
                "<riskdesc>Low (Medium)</riskdesc>\n" +
                "<desc><p>Web Browser XSS Protection is not enabled, or is disabled by the configuration of the 'X-XSS-Protection' HTTP response header on the web server</p></desc>\n" +
                "<instances>\n" +
                "<instance>\n" +
                "<uri>http://192.168.99.100:32768/sitemap.xml</uri>\n" +
                "<method>GET</method>\n" +
                "<param>X-XSS-Protection</param>\n" +
                "</instance>\n" +
                "<instance>\n" +
                "<uri>http://192.168.99.100:32768/robots.txt</uri>\n" +
                "<method>GET</method>\n" +
                "<param>X-XSS-Protection</param>\n" +
                "</instance>\n" +
                "<instance>\n" +
                "<uri>http://192.168.99.100:32768/</uri>\n" +
                "<method>GET</method>\n" +
                "<param>X-XSS-Protection</param>\n" +
                "</instance>\n" +
                "</instances>\n" +
                "<count>3</count>\n" +
                "<solution><p>Ensure that the web browser's XSS filter is enabled, by setting the X-XSS-Protection HTTP response header to '1'.</p></solution>\n" +
                "<otherinfo><p>The X-XSS-Protection HTTP response header allows the web server to enable or disable the web browser's XSS protection mechanism. The following values would attempt to enable it: </p><p>X-XSS-Protection: 1; mode=block</p><p>X-XSS-Protection: 1; report=http://www.example.com/xss</p><p>The following values would disable it:</p><p>X-XSS-Protection: 0</p><p>The X-XSS-Protection HTTP response header is currently supported on Internet Explorer, Chrome and Safari (WebKit).</p><p>Note that this alert is only raised if the response body could potentially contain an XSS payload (with a text-based content type, with a non-zero length).</p></otherinfo>\n" +
                "<reference><p>https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet</p><p>https://blog.veracode.com/2014/03/guidelines-for-setting-security-headers/</p></reference>\n" +
                "<cweid>933</cweid>\n" +
                "<wascid>14</wascid>\n" +
                "<sourceid>3</sourceid>\n" +
                "</alertitem>";
        AlertItemDTO alertItem = Results.getInstance().xmlStringToAlertItem(xmlString);
        System.out.println("Alert name: " + alertItem.getName() + ", Risk: " + alertItem.getRiskcode());
    }

    @Test
    void getResultSetJSON() {
        Results.getInstance().getResultSetJSON();
    }

    @Test
    void append() {
        Results.getInstance().append(new ZapGateway().getXmlReport());
    }

    @Test
    void clearAll() {
    }

}