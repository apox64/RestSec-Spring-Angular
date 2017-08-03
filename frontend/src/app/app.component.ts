import { Component } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
import * as angular from "angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  constructor (private http: Http) {}

  backendURL = 'http://127.0.0.1:8080';
  targetURL = 'http://127.0.0.1:8080';

  downloadResults() {
    console.log("Serving the html results file from backend here ...")
  }

  title = 'app';
}
