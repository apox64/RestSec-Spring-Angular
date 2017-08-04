import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'crawler',
  templateUrl: './crawler.component.html',
  styleUrls: ['./crawler.component.css']
})

export class CrawlerComponent {

  constructor (private http: Http) {}

  backendURL = 'http://127.0.0.1:8080';
  id: number;
  name: string;
  selectedCrawlerType = 'HATEOAS';

  isLoading = false;
  hateoasUrl = "http://localhost:80/api/rest/test"

  crawlerTypes = [
    'Swagger',
    'HATEOAS',
  ]

  addToAttackset() {
    this.isLoading = true;
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  addHateoasURL() {
    console.log()
  }

  swaggerCrawler() { }
  hateoasCrawler() { }

  onTypeChange() {
    console.log('Hello!');
  }

}
