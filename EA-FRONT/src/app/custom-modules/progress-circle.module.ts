import { NgModule } from '@angular/core';
import { NgCircleProgressModule } from 'ng-circle-progress';
import {RoundProgressModule, RoundProgressConfig} from 'angular-svg-round-progressbar';

const config = {
  "radius": 70,
  "space": -5,
  "outerStrokeWidth": 5,
  "outerStrokeColor": "#50f718",
  "innerStrokeColor": "#ebebeb",
  "innerStrokeWidth": 5,
  "animationDuration": 0,
  "titleFontSize": "0",
  "subtitleFontSize": "12",
  "showSubtitle": true,
  "renderOnClick":false,
};

@NgModule({
  imports: [
    //NgCircleProgressModule.forRoot(config),
    RoundProgressModule
  ],
  exports: [
    //NgCircleProgressModule,
    RoundProgressModule,
  ]
})
export class ProgressCircleModule {
  constructor(private _config: RoundProgressConfig) {
    _config.setDefaults({
      radius:70,
      color:"#4CBB17",
      stroke:7,
      duration:2000,
    });
  }
 }
