import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MaterialModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CdkTableModule } from '@angular/cdk';
import { MdSortModule } from '@angular/material';

import { AppComponent } from './app.component';
import { AttacksetComponent } from './attackset/attackset.component';
import { CrawlerComponent } from './crawler/crawler.component';
import { ScannerComponent } from './scanner/scanner.component';
import { ReportingComponent } from './reporting/reporting.component';
import { ResultsComponent } from './results/results.component';

import { AttacksetService } from './attackset/attackset.service';

import 'hammerjs';

@NgModule({
  declarations: [
    AppComponent,
    AttacksetComponent,
    CrawlerComponent,
    ScannerComponent,
    ReportingComponent,
    ResultsComponent
  ],
  imports: [
    BrowserModule,
    HttpModule,
    HttpClientModule,
    FormsModule,
    MaterialModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    CdkTableModule,
    MdSortModule
  ],
  exports: [
    //MdSortModule
  ],
  providers: [AttacksetService],
  bootstrap: [AppComponent]
})

export class AppModule { }
