import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';
import { URLSearchParams } from '@angular/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'scanner',
  templateUrl: './scanner.component.html',
  styleUrls: ['./scanner.component.css']
})

export class ScannerComponent {

  constructor(private http: Http) { }

  // id: number;
  // name: string;
  zapReachable = false;
  zapOnlineStatus = "OFFLINE";
  sqlmapReachable = false;
  sqlmapOnlineStatus = "OFFLINE";

  zapUrl = 'http://127.0.0.1:8081';
  targetURL = 'http://192.168.99.100:32768';
  // scanInProgress = 'no attack running';
  addAllHttpVerbs: boolean;

  scanners = {
    'zap': {
      'checked': true,
      'spider': {
        'finished': false,
        'progress': 0
      },
      'scanner': {
        'finished': false,
        'progress': 0
      }
    },
    'sqlmap': {
      'checked': true,
      'finished': false,
      'running': false
    },
    'restsec': {
      'xss': {
        'checked': true,
        'finished': false,
        'running': false
      },
      'headers': {
        'checked': true,
        'finished': false,
        'running': false
      }
    }
  }

  checkAll() {
    this.scanners.zap.checked = true;
    this.scanners.sqlmap.checked = true;
    this.scanners.restsec.xss.checked = true;
    this.scanners.restsec.headers.checked = true;
  }

  uncheckAll() {
    this.scanners.zap.checked = false;
    this.scanners.sqlmap.checked = false;
    this.scanners.restsec.xss.checked = false;
    this.scanners.restsec.headers.checked = false;
  }

  // getScanners() {
  //   return this.scanners;
  // }

  finishedMock() {
    this.scanners.zap.spider.finished = !this.scanners.zap.spider.finished;
    this.scanners.zap.scanner.finished = !this.scanners.zap.scanner.finished;
    this.scanners.sqlmap.finished = !this.scanners.sqlmap.finished;
    this.scanners.restsec.xss.finished = !this.scanners.restsec.xss.finished;
    this.scanners.restsec.headers.finished = !this.scanners.restsec.headers.finished;
  }

  runAllSelected() {
    if (this.scanners.zap.checked) { this.scanners.zap.spider.finished = false; this.scanners.zap.scanner.finished = false; this.runZap() }
    if (this.scanners.sqlmap.checked) { this.scanners.sqlmap.finished = false; this.runSqlmap() }
    if (this.scanners.restsec.xss.checked) { this.scanners.restsec.xss.finished = false; this.runRestsecXssScanner() }
    if (this.scanners.restsec.headers.checked) { this.scanners.restsec.headers.finished = false; this.runRestsecHeaderScanner() }
  }

  runZap() {
    this.isZapOnline();
    this.startZapSpiderScanner();
  }

  runSqlmap() {
    this.scanners.sqlmap.running = true;
    this.http.get('/scanner/sqlmap/start')
      .subscribe(
      data => {
        const res = data.json();
        console.log(res)
        if (res["status"] == "OK") {
          this.scanners.sqlmap.finished = true;
        }
      },
      error => console.log(error),
      () => {
        this.scanners.sqlmap.running = false;
      }
      )
  }

  runRestsecXssScanner() {
    this.scanners.restsec.xss.running = true;
    this.http.get('/scanner/restsec/xss/start')
      .subscribe(
      data => {
        const res = data.json();
        console.log(res)
        if (res["status"] == "OK") {
          this.scanners.restsec.xss.finished = true;
        }
      },
      error => console.log(error),
      () => {
        this.scanners.restsec.xss.running = false;
      }
      )
  }

  runRestsecHeaderScanner() {
    this.scanners.restsec.headers.running = true;
    this.http.get('/scanner/restsec/header/start')
      .subscribe(
      data => {
        const res = data.json();
        console.log(res)
        if (res["status"] == "OK") {
          this.scanners.restsec.headers.finished = true;
        }
      },
      error => console.log(error),
      () => {
        this.scanners.restsec.headers.running = false;
      }
      )
  }

  isZapOnline() {
    this.http.get('/scanner/zap', { params: { "zapUrl": this.zapUrl } })
      .subscribe(
      (res: Response) => {
        const statusOnline = res.json();
        console.log(statusOnline);
        if (res.json()["zap"] == "online") {
          this.zapReachable = true;
          this.zapOnlineStatus = "ONLINE";
        } else {
          this.zapReachable = false;
          this.zapOnlineStatus = "OFFLINE";
        }
      }
      )
  }

  startZapSpiderScanner() {
    this.scanners.zap.spider.progress = 0;
    this.scanners.zap.scanner.progress = 0;
    let parameters = new URLSearchParams();
    parameters.append('url', this.targetURL);
    // console.log(parameters)
    this.http.post('/scanner/zap/start', parameters)
      .subscribe(
      (res: Response) => {
        const response = res.json();
        // console.log("Answer from /scanner/zap/start")
        // console.log(response);
        if (response["status"] != "OK") {
          console.log("something went wrong on the backend (zap).")
        }
      }
      );
    Observable.interval(1000)
      .takeWhile(() => !this.scanners.zap.spider.finished)
      .subscribe(i => this.getZapSpiderStatus())

    Observable.interval(1000)
      .takeWhile(() => !this.scanners.zap.scanner.finished)
      .subscribe(i => this.getZapScannerStatus())
  }

  getZapSpiderStatus() {
    this.http.get('/scanner/zap/status', { params: { "type": "spider" } })
      .subscribe(
      (res: Response) => {
        const spiderStatus = res.json();
        var highest = Object.keys(spiderStatus.spider).sort().pop();
        // console.log(spiderStatus.spider[highest].progress)
        this.scanners.zap.spider.progress = spiderStatus.spider[highest].progress;
        if (this.scanners.zap.spider.progress == 100) {
          this.scanners.zap.spider.finished = true;
        }
      }
      );
  }

  getZapScannerStatus() {
    this.http.get('/scanner/zap/status', { params: { "type": "ascan" } })
      .subscribe(
      (res: Response) => {
        const scannerStatus = res.json();
        var highest = Object.keys(scannerStatus.ascan).sort().pop();
        // console.log(scannerStatus.ascan[highest].progress)
        this.scanners.zap.scanner.progress = scannerStatus.ascan[highest].progress;
        if (this.scanners.zap.scanner.progress == 100) {
          this.scanners.zap.scanner.finished = true;
        }
      }
      );
  }

}
