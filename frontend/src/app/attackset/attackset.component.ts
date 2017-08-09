import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'attackset',
  templateUrl: './attackset.component.html',
  styleUrls: ['./attackset.component.css']
})

export class AttacksetComponent {

  constructor(private http: Http) { }

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

  httpVerbs = [
    'GET',
    'POST',
    'PUT',
    'DELETE',
    'PATCH',
    'UPDATE',
    'HEAD'
  ]

  id: number;
  name: string;

  getAttackset() {
    this.http.get('/reporting/attackset')
      .subscribe(
      (res: Response) => {
        const response = res.json();
        this.attackSet = response;
        console.log(this.attackSet);
      });
  }

}
