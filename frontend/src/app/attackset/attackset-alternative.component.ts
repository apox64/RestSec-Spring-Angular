import { Component, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
// import { DataSource } from '@angular/cdk';
import { MdSort } from '@angular/material';
// import { BehaviorSubject } from 'rxjs/BehaviorSubject';
// import { Observable } from 'rxjs/Observable';
import { AttacksetService } from './attackset.service';
import { OnInit } from '@angular/core';
import { AttackableEndpoint } from './attackableEndpoint';
// import 'rxjs/add/operator/startWith';
// import 'rxjs/add/observable/merge';
// import 'rxjs/add/operator/map';

@Component({
  selector: 'attackset-alternative',
  templateUrl: './attackset.component.html',
  styleUrls: ['./attackset.component.css'],
  providers: [ AttacksetService ]
})

export class AttacksetComponent implements OnInit {
  displayedColumns = [ 'endpointUrl', 'httpVerb', 'scanStatus', 'delete' ];

  numberOfEndpoints: number;
  endpointToDelete: string;
  // endpointUrl: string;
  // httpVerb = 'GET';
  // httpVerbs = [ 'GET', 'POST', 'PUT', 'DELETE' ]

  @ViewChild(MdSort) sort: MdSort;

  public attackset: AttackableEndpoint[];
  getData: string;

  constructor(private _attacksetService: AttacksetService) {}

  getAttackset() {
    console.log("Getting Attackset ...");
    this._attacksetService.getAttackset()
      .subscribe(
        data => this.getData = JSON.stringify(data),
        error => console.log(error),
        () => console.log("Done.")
      );
    //.then((attackset: AttackableEndpoint[]) => this.attackset = attackset);
  }

  deleteByID(id: string) {
    console.log("Deleting Endpoint : " + id);
    this._attacksetService.deleteAttackableEndpoint(id)
      .subscribe(
        data => console.log(data),
        error => console.log(error),
        () => console.log("Done.")
      )
  }

  ngOnInit():any {
    // this.dataSource = new EndpointDataSource(this.endpointDatabase, this.sort);
    // this.numberOfEndpoints = this.endpointDatabase.getNumberOfEndpoints();
  }

}
