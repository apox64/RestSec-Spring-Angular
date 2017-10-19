import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';
import { URLSearchParams } from '@angular/http';
import { MdSnackBar } from '@angular/material';
import { Observable } from 'rxjs';

import { AttacksetService } from '../attackset/attackset.service';

@Component({
  selector: 'scanner',
  templateUrl: './scanner.component.html',
  styleUrls: ['./scanner.component.css']
})

export class ScannerComponent {

  constructor(private http: Http, public snackBar: MdSnackBar, private attacksetService: AttacksetService) { }

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
      'checked': false,
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
      'checked': false,
      'finished': false,
      'running': false
    },
    'restsec': {
      'xss': {
        'checked': false,
        'finished': false,
        'running': false
      },
      'headers': {
        'checked': false,
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

  finishedMock() {
    this.scanners.zap.spider.finished = !this.scanners.zap.spider.finished;
    this.scanners.zap.scanner.finished = !this.scanners.zap.scanner.finished;
    this.scanners.sqlmap.finished = !this.scanners.sqlmap.finished;
    this.scanners.restsec.xss.finished = !this.scanners.restsec.xss.finished;
    this.scanners.restsec.headers.finished = !this.scanners.restsec.headers.finished;
  }

  runAllSelected() {
    if (!this.scanners.zap.checked && !this.scanners.sqlmap.checked && !this.scanners.restsec.xss.checked && !this.scanners.restsec.headers.checked) {
      this.snackBar.open("Please select at least one scanner.", "OK", {
        duration: 5000
      });
    }
    this.attacksetService.getAll()
      .subscribe(
      data => {
        if (data.length == 0) {
          this.snackBar.open("No Endpoints specified.", "OK", {
            duration: 5000
          });
        } else {
          if (this.scanners.zap.checked) { this.scanners.zap.spider.finished = false; this.scanners.zap.scanner.finished = false; this.runZap() }
          if (this.scanners.sqlmap.checked) { this.scanners.sqlmap.finished = false; this.runSqlmap() }
          if (this.scanners.restsec.xss.checked) { this.scanners.restsec.xss.finished = false; this.runRestsecXssScanner() }
          if (this.scanners.restsec.headers.checked) { this.scanners.restsec.headers.finished = false; this.runRestsecHeaderScanner() }
        }
        console.log(data);
      },
      error => console.log(error),
      () => {
        console.log("getAll(): done.")
      }
      );
  }

  runZap() {
    this.isZapOnline();
    if (this.zapReachable) {
      this.startZapSpiderScanner();
    } else {
      console.log("zap not reachable");
    }
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
          this.getZapSpiderStatus();
          this.getZapScannerStatus();
          this.zapOnlineStatus = "ONLINE";
        } else {
          this.zapReachable = false;
          this.zapOnlineStatus = "OFFLINE";
        }
      }
      )
  }

  startZapSpiderScanner() {
    console.log("starting zap spider and scanner ...")
    this.scanners.zap.spider.progress = 0;
    this.scanners.zap.scanner.progress = 0;
    let parameters = new URLSearchParams();
    parameters.append('url', this.targetURL);
    // console.log(parameters)
    this.http.post('/scanner/zap/start', parameters)
      .subscribe(
      (res: Response) => {
        const response = res.json();
        console.log("Answer from /scanner/zap/start")
        console.log(response);
        if (response["status"] != "OK") {
          console.log("something went wrong on the backend (zap)?")
        }
      }
      );

    Observable.interval(2000)
      .takeWhile(() => !this.scanners.zap.spider.finished)
      .subscribe(() => this.getZapSpiderStatus()
      )

    Observable.interval(2000)
      .takeWhile(() => !this.scanners.zap.scanner.finished)
      .subscribe(() => this.getZapScannerStatus()
      )
  }

  getZapSpiderStatus() {
    this.http.get('/scanner/zap/status', { params: { "type": "spider" } })
      .subscribe(
      data => {
        const spiderStatus = data.json();
        console.log(spiderStatus);
        this.scanners.zap.spider.progress = spiderStatus.spider;
        if (this.scanners.zap.spider.progress == 100) {
          this.scanners.zap.spider.finished = true;
        } else {
          this.scanners.zap.spider.finished = false;
        }
      },
      error => console.log("something went wrong")
      )
  }

  getZapScannerStatus() {
    this.http.get('/scanner/zap/status', { params: { "type": "ascan" } })
      .subscribe(
      (res: Response) => {
        const scannerStatus = res.json();
        this.scanners.zap.scanner.progress = scannerStatus.ascan;
        if (this.scanners.zap.scanner.progress == 100) {
          this.scanners.zap.scanner.finished = true;
        } else {
          this.scanners.zap.scanner.finished = false;
        }
      }
      );
  }

  clearZapSessions() {
    this.http.get('/scanner/zap/clear')
      .subscribe(
      (res: Response) => {
        const status = res.json();
        console.log("clearZapSessions() : " + status);
      }
      )
  }

}
