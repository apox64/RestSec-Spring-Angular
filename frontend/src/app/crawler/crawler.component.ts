import { Component } from '@angular/core';
import { HttpParams, HttpHeaders, HttpClient, HttpResponse } from '@angular/common/http';
import { Http, Response, RequestOptions, URLSearchParams, Headers } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { HttpClientModule } from '@angular/common/http';
import { MdSnackBar } from '@angular/material';
// import { HttpClient } from './crawler.http.service';
// import { map } from 'rxjs/operator/map';

@Component({
  selector: 'crawler',
  templateUrl: './crawler.component.html',
  styleUrls: ['./crawler.component.css']
})

export class CrawlerComponent {

  constructor(private http: Http, public snackBar: MdSnackBar) { }

  selectedCrawlerType = 'Swagger';
  targetAuthToken = 'fd0847dbb559752d932dd3c1ac34ff98d27b11fe2fea5a864f44740cd7919ad08d27b11fe2fea5a864f44740cd7919ad045234523451f';

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
      console.log("doing nothing ... Swagger scans on upload.");
    } else if (this.selectedCrawlerType == "HATEOAS") {
      this.hateoasCrawler();
    }
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  swaggerCrawler(event): void {
    let fileList: FileList = event.target.files;
    console.log("Number of files to upload: " + fileList.length)
    if(fileList.length > 0) {
        let file: File = fileList[0];
        let formData:FormData = new FormData();
        formData.append('uploadFile', file, file.name);
        let headers = new Headers();
        /** No need to include Content-Type in Angular 4 */
        // headers.append('Content-Type', 'multipart/form-data');
        headers.append('Accept', 'application/json');
        let options = new RequestOptions({ headers: headers });
        this.http.post('crawler/swagger/upload', formData, options)
            .map(res => res.json())
            .subscribe(
                data => {
                  console.log(data),
                  this.numberOfAttackPointsFound = data;
                  this.snackBar.open("Number of AttackPoints found: " + this.numberOfAttackPointsFound, "OK", {
                    duration: 5000
                  });
                },
                error => console.log(error)
            )
    }
  }

  hateoasCrawler(): void {
    console.log('url: ' + this.hateoasUrl + "; targetAuthToken: " + this.targetAuthToken)

    let data = {
      'url' : this.hateoasUrl,
      'targetAuthToken' : this.targetAuthToken
    }

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    this.http.post('crawler/hateoas', JSON.stringify(data), {headers: headers})
    .subscribe(
      (res: any) => {
        this.numberOfAttackPointsFound = res;
        this.snackBar.open("Number of AttackPoints found: " + this.numberOfAttackPointsFound, "OK", {
          duration: 5000
        });
        console.log(res);
    });

  }

}
