import { Injectable } from '@angular/core';
import { ControlService } from '../services/control.service';
@Injectable()
export class DatasetDesignService {

  constructor(private cs: ControlService) { }


  getRowClass(valid, running, message, category) {
    if (running) return 'animateBG table-info';
    if (valid == 'yes') return 'animateBG table-success';
    if (valid == 'no') {
     if(message == 'Not Executed') {
      return 'animateBG table-notExecuted';
     } else {
      if(category == "Warning") {
        return "animateBG table-warn";
      }else {
        return 'animateBG table-danger';
      }
     }
    }
    return 'animateBG'
  }
  getRowClassExemples(type,category) {
    if(category == 'Warning') return 'table-'+type+'-warn'
    return  'table-'+type
  }

  icon(valid) {
    if (valid == 'yes') return 'check'
    if (valid == 'no') return 'times'
  }

  excecutionInfo = (fileType: string): string => {
    return this.cs.excecutionInfo(fileType)
  }

  progressColor(fileType) {
    if (this.cs.progressPerc(fileType) == 100) return 'rgb(121, 240, 117)'
    else return '#4882c2'
  }}
