import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { DataSource } from '@angular/cdk';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Observable } from 'rxjs/Observable';
import { MdSort } from '@angular/material';
import { AttackableEndpoint } from './attackableEndpoint'
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import 'rxjs/add/operator/map';

@Injectable()
export class AttacksetService {

  constructor (private _http: Http) {}

  getAll() {
    return this._http.get('reporting/attackset')
      .map(res => res.json());
  }

  add(endpointUrl: string, httpVerb: string) {
    const attackableEndpoint = {
      endpointUrl: endpointUrl,
      httpVerb: httpVerb,
      scanStatus : false
    }
    console.log("attackset.service : add(" + attackableEndpoint.endpointUrl + ", " + attackableEndpoint.httpVerb + ", " + attackableEndpoint.scanStatus + ")")
    return this._http.post('reporting/attackset', attackableEndpoint)
      .map(res => res.json());
  }

  delete(id: string) {
    console.log("attackset.service : delete(" + id + ")")
    return this._http.delete('reporting/attackset/' + id)
      .map(res => res.json());
  }

  deleteAll() {
    console.log("attackset.service : deleteAll()")
    return this._http.delete('reporting/attackset')
      .map(res => res.json());
  }

}



/*
export class EndpointDatabase {
  dataChange: BehaviorSubject<EndpointData[]> = new BehaviorSubject<EndpointData[]>([]);

  private attacksetService: AttacksetService;

  get data(): EndpointData[] { return this.dataChange.value; }

  constructor() {
  }

  getDataFromBackend() {
    const copiedData = this.data.slice();
    console.log("Getting Attackset ...");
    let remoteData: EndpointData[];
    this.attacksetService.getAttackset()
      .subscribe(
        data => {
          remoteData = this.data;
          console.log(remoteData)
          console.log(this.data)
        },
        error => console.log(error),
        () => console.log("Done.")
      );
    const remoteDataFixed = remoteData.slice();
    // copiedData.push(remoteDataFixed);
    this.attacksetService.getAttackset();
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

  private removeEndpoint(id: number) {
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

  private removeAll() {
    const copiedData = [];
    this.dataChange.next(copiedData);
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

*/

/*
export class EndpointDataSource extends DataSource<any> {
  constructor(private _endpointDatabase: EndpointDatabase, private _sort: MdSort) {
    super();
  }

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

*/
