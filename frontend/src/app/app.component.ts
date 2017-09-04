import { Component, Input, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
import { ScannerComponent } from './scanner/scanner.component';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  host: {
    "[class.dark-theme]": "darkMode",
    "[class.light-theme]": "!darkMode"
  },
})

export class AppComponent {

  @ViewChild(ScannerComponent)
  private scannerComponent: ScannerComponent;

  constructor(private _http: Http) { }

  "darkMode": boolean = false;
  targetURL = 'http://127.0.0.1:8080';
  selectedIndex = 2;
  isLoading = false;

  runAttack() {
    // this.isLoading = true;
    this.scannerComponent.runAllSelected()
    // this.isLoading = false;
  }

  onSelect() {
    // if (this.selectedIndex == 0) {
    //   this.attacksetComponent.getAttackset();
    // }
  }



  title = 'app';
}
