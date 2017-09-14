package de.novatecgmbh.restsecspring.logic.reporting.results;

public class ResultsetDTO {

    /*
    * structure
    *
    * [
    *   { "OWASPZapReport" : {
    *           "high" : 5,
    *           "medium" : 3,
    *           "low" : 1
    *       }
    *   },
    *   { "sqlmap" : {
    *           "high" : 12,
    *           "medium" : 0,
    *           "low" : 5
    *       }
    *   },
    *   { "otherScanner" ...
    *   }
    * ]
    *
    * */

    private ZapReportDTO zapReportDTO;


}
