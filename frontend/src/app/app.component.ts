import { Component } from '@angular/core';
import { Http } from '@angular/http';
import { AttacksetComponent } from './attackset/attackset.component';
import { AttacksetService } from './attackset/attackset.service';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  host: {
    "[class.dark-theme]": "darkMode",
    "[class.light-theme]": "!darkMode"
  },
  providers: [AttacksetComponent, AttacksetService]
})

export class AppComponent {

  constructor(private attacksetComponent : AttacksetComponent) { }

  "darkMode": boolean = false;
  targetURL = 'http://127.0.0.1:8080';
  selectedIndex = 2;
  isLoading = false;

  runAttack() {
    this.isLoading = true;
    setTimeout(
      () => this.isLoading = false,
      3000
    );
  }

  onSelect() {
    // if (this.selectedIndex == 0) {
    //   this.attacksetComponent.getAttackset();
    // }
  }

  title = 'app';
}
