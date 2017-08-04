import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'attackset',
  templateUrl: './attackset.component.html',
  styleUrls: ['../app.component.css']
})

export class AttacksetComponent {

  constructor (private http: Http) {}

  attackSet = [
    {
        "endpointURL": "testurl",
        "scanStatus": false,
        "id": "9ac9644a-3999-4e71-9890-4fe38e1079ba"
    },
    {
        "endpointURL": "testurl2",
        "scanStatus": true,
        "id": "6f614de2-5bce-411e-8866-47b71bc78776"
    }
  ];
  backendURL = 'http://127.0.0.1:8080';
  id: number;
  name: string;

  getAttackset() {
    this.http.get(this.backendURL + '/reporting/attackset')
    .subscribe(
      (res: Response) => {
        const response = res.json();
        this.attackSet = response;
        console.log(this.attackSet);
      }
    )
  }

}
