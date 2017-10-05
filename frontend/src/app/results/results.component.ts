import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
  dataSource = new ExampleDataSource();
  constructor(private http: HttpClient) { }
}

export interface Element {
  type: string;
  low: number;
  mid: number;
  high: number;
}

const data: Element[] = [
  { type: 'OWASP Zap Proxy', low: 0, mid: 0, high: 0 },
  { type: 'sqlmap', low: 0, mid: 0, high: 0 },
  { type: 'RestSec XSS Scanner', low: 0, mid: 0, high: 0 },
  { type: 'RestSec Security Header Scanner', low: 0, mid: 0, high: 0 },
];

export class ExampleDataSource extends DataSource<any> {
  /** Connect function called by the table to retrieve one stream containing the data to render. */
  connect(): Observable<Element[]> {
    return Observable.of(data);
  }

  disconnect() { }
}
