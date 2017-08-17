import { Component, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
import { MdSort } from '@angular/material';
import { AttacksetService } from './attackset.service';


@Component({
  selector: 'attackset',
  templateUrl: './attackset.component.html',
  styleUrls: ['./attackset.component.css'],
  providers: [ AttacksetService ]
})

export class AttacksetComponent {
  displayedColumns = [ 'endpointUrl', 'httpVerb', 'scanStatus', 'delete' ];
  // endpointDatabase = new EndpointDatabase();
  // dataSource: EndpointDataSource | null;

  numberOfEndpoints: number;
  endpointUrl: string;
  httpVerb = 'GET';
  httpVerbs = [ 'GET', 'POST', 'PUT', 'DELETE' ]

  @ViewChild(MdSort) sort: MdSort;

  constructor(private attacksetService: AttacksetService) { }

  getAttackset() {
    this.attacksetService.getAll()
      .subscribe(
        data => {
          console.log(data)
        },
        error => console.log(error),
        () => console.log("done.")
      );
  }

  addEndpoint(endpointUrl: string, httpVerb: string) {
    this.attacksetService.add(this.endpointUrl, this.httpVerb)
      .subscribe(
        data => {
          console.log(data)
        },
        error => console.log(error),
        () => console.log("done.")
      );
  }

  remove(id: string) {
    this.attacksetService.delete(id)
      .subscribe(
        data => {
          console.log(data)
        },
        error => console.log(error),
        () => console.log("done.")
      );
  }

  removeAll() {
    this.attacksetService.deleteAll()
    .subscribe(
      data => {
        console.log(data)
      },
      error => console.log(error),
      () => console.log("done.")
    );
  }

  ngOnInit() {
  }

}
