import { Component } from '@angular/core';
import { Http, Response, URLSearchParams } from '@angular/http';
// import * as angular from "angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  constructor(private http: Http) { }

  targetURL = 'http://127.0.0.1:8080';
  selectedTab = 1;
  isLoading = false;

  runAttack() {
    this.isLoading = true;
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  title = 'app';
}
