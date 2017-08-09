import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpParams, HttpHeaders } from '@angular/common/http';
import { Http, Response, RequestOptions, URLSearchParams, Headers } from '@angular/http';
import { HttpResponse } from '@angular/common/http';
// import { HttpClient } from './crawler.http.service';
// import { map } from 'rxjs/operator/map';

@Component({
  selector: 'crawler',
  templateUrl: './crawler.component.html',
  styleUrls: ['./crawler.component.css']
})

export class CrawlerComponent {

  constructor(private http: HttpClient) { }

  selectedCrawlerType = 'HATEOAS';
  targetAuthToken = 'fd0847dbb559752d932dd3c1ac34ff98d27b11fe2fea5a864f44740cd7919ad0';

  isLoading = false;
  hateoasUrl = "http://localhost:10001/albums"
  numberOfAttackPointsFound = 0;

  crawlerTypes = [
    'Swagger',
    'HATEOAS',
  ]

  addToAttackset() {
    this.isLoading = true;
    if (this.selectedCrawlerType == "Swagger") {
      this.swaggerCrawler();
    } else if (this.selectedCrawlerType == "HATEOAS") {
      this.hateoasCrawler();
    }
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  swaggerCrawler(): void {

  }

  hateoasCrawler(): void {
    console.log('url: ' + this.hateoasUrl + "; targetAuthToken: " + this.targetAuthToken)


    let data = {
      'url' : this.hateoasUrl,
      'targetAuthToken' : this.targetAuthToken
    }

    this.http.post('crawler/hateoas', JSON.stringify(data), {headers: new HttpHeaders().set('Content-Type','application/json')})
    .subscribe(
      (res: any) => {
        this.numberOfAttackPointsFound = res;
        console.log(res);
    });

  }

}
