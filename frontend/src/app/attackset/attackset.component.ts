import { Component, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
import { DataSource } from '@angular/cdk';
import { MdSort } from '@angular/material';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import 'rxjs/add/operator/map';

@Component({
  selector: 'attackset',
  templateUrl: './attackset.component.html',
  styleUrls: ['./attackset.component.css'],
})

export class AttacksetComponent {
  displayedColumns = ['id', 'endpointUrl', 'httpVerb', 'scanStatus', 'delete'];
  endpointDatabase = new EndpointDatabase();
  dataSource: EndpointDataSource | null;

  numberOfEndpoints: number;
  endpointUrl: string; // = 'https://my.api.local/rest/';
  httpVerb = 'GET';
  httpVerbs = [ 'GET', 'POST', 'PUT', 'DELETE' ]

  @ViewChild(MdSort) sort: MdSort;

  removeEndpoint(id: number) {
    console.log("Removing Endpoint with id = " + id);
    this.endpointDatabase.removeEndpoint(id);
    this.numberOfEndpoints = this.endpointDatabase.getNumberOfEndpoints();
  }

  addEndpoint() {
    this.endpointDatabase.addEndpoint(this.endpointUrl, this.httpVerb);
    this.numberOfEndpoints = this.endpointDatabase.getNumberOfEndpoints();
  }

  ngOnInit() {
    this.dataSource = new EndpointDataSource(this.endpointDatabase, this.sort);
    this.numberOfEndpoints = this.endpointDatabase.getNumberOfEndpoints();
  }

  removeAll() {
    this.endpointDatabase.removeAll();
    this.numberOfEndpoints = this.endpointDatabase.getNumberOfEndpoints();
  }
}

const URLS = [
  'http://test.local:8080/api/',
  'https://my.website.com:18229/api/rest',
  'http://test.local:8081/api/rest/test/user/my/new/endpoint'
]
const VERBS = [ 'GET', 'POST', 'PUT', 'DELETE' ]

export interface EndpointData {
  id: string;
  endpointUrl: string;
  httpVerb: string;
  scanStatus: boolean;
}

export class EndpointDatabase {
  dataChange: BehaviorSubject<EndpointData[]> = new BehaviorSubject<EndpointData[]>([]);

  get data(): EndpointData[] {
    return this.dataChange.value;
  }

  constructor() {
    for (let i = 0; i < 5; i++) { this.addSampleEndpoints(); }
  }

  addSampleEndpoints() {
    const copiedData = this.data.slice();
    copiedData.push(this.createNewRandomEndpoint());
    this.dataChange.next(copiedData);
  }

  addEndpoint(endpointUrl: string, httpVerb: string) {
    const copiedData = this.data.slice();
    copiedData.push(this.createNewEndpoint(endpointUrl, httpVerb));
    this.dataChange.next(copiedData);
  }

  getNumberOfEndpoints() {
    const copiedData = this.data.slice();
    return copiedData.length;
  }

  removeEndpoint(id: number) {
    const copiedData = this.data.slice();
    console.log(copiedData);

    for (var endpoint of copiedData) {
      if (Number(endpoint.id) == id) {
        var index = copiedData.indexOf(endpoint);
      }
    }

    copiedData.splice(index, 1);
    this.dataChange.next(copiedData);
  }

  removeAll() {
    const copiedData = [];
    this.dataChange.next(copiedData);
  }

  private createNewRandomEndpoint() {
    const url = URLS[Math.round(Math.random() * (URLS.length - 1))];
    const verb = VERBS[Math.round(Math.random() * (VERBS.length - 1))];
    const scanStatus = Math.random() >= 0.5;
    return {
      id: (this.data.length + 1).toString(),
      endpointUrl: url,
      httpVerb: verb,
      scanStatus: scanStatus
    };
  }

  private createNewEndpoint(endpointUrl: string, httpVerb: string) {
    return {
      id: (this.data.length + 1).toString(),
      endpointUrl: endpointUrl,
      httpVerb: httpVerb,
      scanStatus: false
    }
  }

}

export class EndpointDataSource extends DataSource<any> {
  constructor(private _endpointDatabase: EndpointDatabase, private _sort: MdSort) {
    super();
  }

  /** Connect function called by the table to retrieve one stream containing the data to render. */
  connect(): Observable<EndpointData[]> {
    const displayDataChanges = [
      this._endpointDatabase.dataChange,
      this._sort.mdSortChange,
    ];

    return Observable.merge(...displayDataChanges).map(() => {
      return this.getSortedData();
    });
  }

  disconnect() { }

  /** Returns a sorted copy of the database data. */
  getSortedData(): EndpointData[] {
    const data = this._endpointDatabase.data.slice();
    if (!this._sort.active || this._sort.direction == '') { return data; }

    return data.sort((a, b) => {
      let propertyA: number | string | boolean = '';
      let propertyB: number | string | boolean = '';

      switch (this._sort.active) {
        case 'id': [propertyA, propertyB] = [a.id, b.id]; break;
        case 'endpointUrl': [propertyA, propertyB] = [a.endpointUrl, b.endpointUrl]; break;
        case 'httpVerb': [propertyA, propertyB] = [a.httpVerb, b.httpVerb]; break;
        case 'scanStatus': [propertyA, propertyB] = [a.scanStatus, b.scanStatus]; break;
      }

      let valueA = isNaN(+propertyA) ? propertyA : +propertyA;
      let valueB = isNaN(+propertyB) ? propertyB : +propertyB;

      return (valueA < valueB ? -1 : 1) * (this._sort.direction == 'asc' ? 1 : -1);
    });
  }
}
