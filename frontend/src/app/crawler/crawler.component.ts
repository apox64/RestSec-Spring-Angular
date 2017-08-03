import { Component } from '@angular/core';
import { Http, Response } from '@angular/http';

@Component({
  selector: 'crawler',
  templateUrl: './crawler.component.html',
  styleUrls: ['../app.component.css']
})

export class CrawlerComponent {

  constructor (private http: Http) {}

  backendURL = 'http://127.0.0.1:8080';
  id: number;
  name: string;

  swaggerCrawler() { }
  hateoasCrawler() { }

}
