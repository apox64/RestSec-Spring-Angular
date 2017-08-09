import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'scanner',
  templateUrl: './scanner.component.html',
  styleUrls: ['./scanner.component.css']
})

export class ScannerComponent {

  constructor(private http: Http) { }

  id: number;
  name: string;

  zapUrl = 'http://127.0.0.1:8081';
  targetURL = 'http://127.0.0.1:8080';
  spiderProgress = 35;
  scannerProgress = 66;
  scanInProgress = 'no attack running';
  addAllHttpVerbs: boolean;

  isFinished = {
    'zapSpider': false,
    'zapScanner': false,
    'sqlmap': false,
    'restsecXSS': false,
    'restsecHeaders': false
  };

  progressSpinnerZap = {
    'mode': 'determinate',
    'color': 'primary'
  };

  primaryScanners = [
    'OWASP Zap Proxy',
    'sqlmap'
  ]

  restsecScanners = [
    'RestSec XSS Scanner',
    'RestSec Security Header Scanner'
  ]

  isZapOnline() {
    this.http.get('/scanner/zap', { params: { "zapUrl": this.zapUrl } })
      .subscribe(
      (res: Response) => {
        const statusOnline = res.json();
        console.log(statusOnline);
        // this.zapOnlineStatus = statusOnline.zap;
        // if (this.zapOnlineStatus == 'online') {
        //   document.getElementById("onlineStatusLabel").className = "label label-success"
        // } else {
        //   document.getElementById("onlineStatusLabel").className = "label label-danger"
        // }
      }
      )
  }

  startZapSpiderScanner() {
    this.scanInProgress = 'attack running'
    document.getElementById("attackRunningLabel").className = "label label-danger"
    let parameters = new URLSearchParams();
    parameters.append('url', this.targetURL);
    this.http.post('/scanner/zap/start', parameters)
      .subscribe(
      (res: Response) => {
        const response = res.json();
        console.log(response);
        this.scanInProgress = 'done';
        document.getElementById("attackRunningLabel").className = "label label-success";
        this.getZapStatus();
        (<HTMLInputElement>document.getElementById("downloadButton")).disabled = false;
      }
      );
  }

  getZapStatus() {
    this.http.get('/scanner/zap/status', { params: { "type": "spider" } })
      .subscribe(
      (res: Response) => {
        const spiderStatus = res.json();
        console.log(spiderStatus);
        var highest = Object.keys(spiderStatus.spider).sort().pop();
        console.log("Accessing element " + highest + " in spider status Object");
        // this.spiderStatus = spiderStatus.spider[highest].state;
        this.spiderProgress = spiderStatus.spider[highest].progress;
      }
      );

    this.http.get('/scanner/zap/status', { params: { "type": "ascan" } })
      .subscribe(
      (res: Response) => {
        const scannerStatus = res.json();
        console.log(scannerStatus);
        var highest = Object.keys(scannerStatus.ascan).sort().pop();
        console.log("Accessing element " + highest + " in scanner status Object");
        // this.scannerStatus = scannerStatus.ascan[highest].state;
        this.scannerProgress = scannerStatus.ascan[highest].progress;
      }
      );
  }

}
