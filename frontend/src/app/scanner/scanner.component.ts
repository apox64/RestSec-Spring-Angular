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
  scanInProgress = 'no attack running';
  addAllHttpVerbs: boolean;

	scanners = {
		'zap': {
			'checked': true,
      'spider': {
        'finished': false,
        'progress': 89
      },
      'scanner': {
        'finished': false,
        'progress': 66
      },
      'spinner': {
        'mode': 'determinate',
        'color': 'primary'
      }
		},
		'sqlmap': {
			'checked': true,
			'finished': false,
      'progress': 'running'
		},
		'restsec': {
			'xss': {
				'checked': true,
				'finished': false,
        'progress': 'running'
			},
			'headers': {
				'checked': true,
				'finished': false,
        'progress': 'running'
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

  finishedTest() {
    this.scanners.zap.spider.finished = !this.scanners.zap.spider.finished;
    this.scanners.zap.scanner.finished = !this.scanners.zap.scanner.finished;
    this.scanners.sqlmap.finished = !this.scanners.sqlmap.finished;
    this.scanners.restsec.xss.finished = !this.scanners.restsec.xss.finished;
    this.scanners.restsec.headers.finished = !this.scanners.restsec.headers.finished;
  }

  isZapOnline() {
    this.http.get('/scanner/zap', { params: { "zapUrl": this.zapUrl } })
      .subscribe(
      (res: Response) => {
        const statusOnline = res.json();
        console.log(statusOnline);
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
        this.scanners.zap.spider.progress = spiderStatus.spider[highest].progress;
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
        this.scanners.zap.scanner.progress = scannerStatus.ascan[highest].progress;
      }
      );
  }

}
