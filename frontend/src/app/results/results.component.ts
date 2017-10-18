import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';
import { DataSource } from '@angular/cdk';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';

@Component({
  selector: 'results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})

export class ResultsComponent {
  displayedColumns = ['type', 'low', 'mid', 'high'];
  dataSource = new SimpleScoresDataSource();
  constructor(private http: Http) { }

  refresh() {
    console.log("refreshing the latest report values ...");
    this.http.get('reporting/results/zap/simple')
    .subscribe(
      data => {
        const res = data.json();
        console.log(res);
        // access values of "data" here ... ?
        for (let e of scores) {
          if (e.type == "OWASP Zap Proxy") {
            e.low = res.riskLow;
            e.mid = res.riskMedium;
            e.low = res.riskLow;
          }
        }
      },
      error => {
        console.log(error);
      }
    )
  }

}

export interface Element {
  type: string;
  low: number;
  mid: number;
  high: number;
}

const scores: Element[] = [
  { type: 'OWASP Zap Proxy', low: 0, mid: 0, high: 0 },
  { type: 'sqlmap', low: 0, mid: 0, high: 0 },
  { type: 'RestSec XSS Scanner', low: 0, mid: 0, high: 0 },
  { type: 'RestSec Security Header Scanner', low: 0, mid: 0, high: 0 },
];

export class SimpleScoresDataSource extends DataSource<any> {
  connect(): Observable<Element[]> {
    return Observable.of(scores);
  }
  disconnect() { }
}
