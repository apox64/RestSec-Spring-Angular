import { Component } from '@angular/core';
import { Http } from '@angular/http';
import { AttacksetComponent } from './attackset/attackset.component';
import { AttacksetService } from './attackset/attackset.service';
// import * as angular from "angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [AttacksetComponent, AttacksetService]
})

export class AppComponent {

  constructor(private attacksetComponent : AttacksetComponent) { }

  darkMode: boolean;
  targetURL = 'http://127.0.0.1:8080';
  selectedIndex = 0;
  isLoading = false;

  runAttack() {
    this.isLoading = true;
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  onSelect() {
    if (this.selectedIndex == 0) {
      this.attacksetComponent.getAttackset();
    }
  }

  title = 'app';
}
