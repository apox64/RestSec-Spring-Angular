import { Component, OnInit, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
import { MdSort } from '@angular/material';
import { DataSource } from '@angular/cdk';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { MdSnackBar } from '@angular/material';
import 'rxjs/add/operator/startWith';
import 'rxjs/add/observable/merge';
import 'rxjs/add/operator/map';


import { AttackableEndpoint } from './attackableEndpoint';
import { AttacksetService } from './attackset.service';

@Component({
  selector: 'attackset',
  templateUrl: './attackset.component.html',
  styleUrls: ['./attackset.component.css'],
})

export class AttacksetComponent implements OnInit {
  public displayedColumns = [ 'endpointUrl', 'httpVerb', 'authToken', 'scanStatus', 'delete' ];
  public endpointDatabase: EndpointDatabase | null;
  public dataSource: EndpointDataSource | null;

  numberOfEndpoints = 0;
  endpointUrl: string;
  httpVerb = 'GET';
  httpVerbs = [ 'GET', 'POST', 'PUT', 'DELETE' ];
  authToken: string;

  @ViewChild(MdSort) sort: MdSort;

  constructor(private _attacksetService: AttacksetService, private _snackBar: MdSnackBar) { }

  getAttackset() {
    console.log("getAttackset()")
    this.endpointDatabase.getAll();
    setTimeout(() => {
        this.numberOfEndpoints = this.endpointDatabase.getLength();
    },
    1000);

    //MOCK HERE FOR JUICE SHOP (default data)
    if (this.numberOfEndpoints == 0) {
      this.endpointDatabase.add("http://192.168.99.101:32768/api/Products/1", "GET", "none");
    }

  }

  addEndpoint() {
    console.log("addEndpoint(\"" + this.endpointUrl + "\", \"" + this.httpVerb + "\", \"" + this.authToken + "\")")
    this.endpointDatabase.add(this.endpointUrl, this.httpVerb, this.authToken);
    this.getAttackset();
  }

  remove(id: string) {
    console.log("remove(\"" + id + "\")")
    this.endpointDatabase.remove(id)
    this.getAttackset();
  }

  removeAll() {
    console.log("removeAll()")
    this.endpointDatabase.removeAll()
    this.getAttackset();
  }

  ngOnInit() {
    this.endpointDatabase = new EndpointDatabase(this._attacksetService, this._snackBar);
    this.dataSource = new EndpointDataSource(this.endpointDatabase, this.sort);
    this.getAttackset();
  }

}

export class EndpointDataSource extends DataSource<any> {
  constructor(private _endpointDatabase: EndpointDatabase, private _sort: MdSort) {
    super();
  }

  connect(): Observable<AttackableEndpoint[]> {
    const displayDataChanges = [
      this._endpointDatabase.dataChange,
      this._sort.mdSortChange,
    ];

    return Observable.merge(...displayDataChanges).map(() => {
      return this.getSortedData();
    });
  }

  disconnect() { }

  getSortedData(): AttackableEndpoint[] {
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

export class EndpointDatabase {

  public dataChange: BehaviorSubject<AttackableEndpoint[]> = new BehaviorSubject<AttackableEndpoint[]>([]);
  get data(): AttackableEndpoint[] { return this.dataChange.value; }

  constructor(private _attacksetService: AttacksetService, private _snackBar: MdSnackBar) { }

  getAll() {
    this._attacksetService.getAll()
    .subscribe(
      data => {
        this.dataChange.next(data);
      },
      error => {
        console.log(error),
        this._snackBar.open("Backend seems to be offline ...", "OK", {
          duration: 2000
        });
      },
      () => {
        console.log("getAll(): done.")
      }
    );
  }

  add(entrypointUrl: string, httpVerb: string, authToken: string) {
    this._attacksetService.add(entrypointUrl, httpVerb, authToken)
    .subscribe(
      data => {
        console.log(data)
      },
      error => console.log(error),
      () => console.log("add: done.")
    )
  }

  remove(id: string) {
    this._attacksetService.delete(id)
    .subscribe(
      data => {
        console.log(data)
      },
      error => console.log(error),
      () => console.log("remove: done.")
    )
  }

  removeAll() {
    this._attacksetService.deleteAll()
    .subscribe(
      data => {
        console.log(data)
      },
      error => console.log(error),
      () => console.log("removeAll(): done.")
    )
  }

  getLength() {
    console.log("Database: " + this.data.length + " entries.")
    return this.data.length;
  }

}
