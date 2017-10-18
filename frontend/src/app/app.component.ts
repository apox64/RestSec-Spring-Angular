import { Component, Input, ViewChild } from '@angular/core';
import { Http, Response } from '@angular/http';
import { ScannerComponent } from './scanner/scanner.component';
import { ResultsComponent } from './results/results.component';
import { AttacksetComponent } from './attackset/attackset.component';

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
  @ViewChild(ResultsComponent)
  private resultsComponent: ResultsComponent;
  @ViewChild(AttacksetComponent)
  private attacksetComponent: AttacksetComponent;

  constructor(private _http: Http) { }

  "darkMode": boolean = false;
  targetURL = 'http://127.0.0.1:8080';
  selectedIndex = 4;
  isLoading = false;

  runAttack() {
    this.scannerComponent.runAllSelected();
  }

  onSelect($event: any) {
    console.log($event);
    if (this.selectedIndex == 0) {
      this.attacksetComponent.getAttackset();
    }
    if (this.selectedIndex == 3) {
       this.resultsComponent.refresh();
    }
  }

  title = 'app';
}
