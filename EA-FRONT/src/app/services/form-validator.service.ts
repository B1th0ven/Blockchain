import {
  Injectable
} from '@angular/core';
import {
  Run
} from '../shared/models/run';
import { date } from '../shared/models/date';
import { Study } from '../shared/models/study';

@Injectable()
export class FormValidatorService {

  constructor() {}

  mandatoryCheck(check: boolean, val: any): boolean {
    if (!check)
      return true;
    if (check && val && val != "" && val != null)
      return true;
    else
      return false;
  }

  checkRadio(val: string): boolean {
    if (val && val != "")
      return true
    return false
  }

  datasetSelected = (run: Run): boolean => {
    if (!run.dataset || run.dataset == "") {
      return false
    }

    return true
  }

  onMetricChanged(run: Run) {
    run.updateDecLimit()
  }

  isDecrementValid(run: Run,study:Study) {
    if ( run.decrements && run.decrements.length >= 1 )
    {
      for ( let dec of run.decrements)
      {
        if (
              !dec.decrement
          ||  dec.decrement == ""
          ||  !this.validDateLowSup(dec.studyStart,dec.studyEnd)
          ||  !this.validDateLowSup(study.startObsDate,dec.studyStart)
          ||  !this.validDateLowSup(dec.studyEnd,study.endObsDate)
        )
          return false
      }
      return true
    }else
    {
      return false;
    }
  }

  isNumber(val): boolean {
    if (!val) val = "1";
    let regex = /^[0-9]+$/;
    if (!String(val).match(regex)) {
      return false;
    }
    return true && parseInt(val) > 0;
  }

  isRunFilesValid(run:Run):boolean
  {
    switch(run.ibnr_method)
    {
      case "" : return true;
      case "manual": if ( run.ibnr_udf_path && run.ibnr_udf_path != "" ) return true; else break;
      case "amount": if ( run.ibnr_allocation_path && run.ibnr_amount_path && run.ibnr_allocation_path != "" && run.ibnr_amount_path != "" ) return true;else break;
    }
    return false
  }

  isRunValid(run: Run,study:Study): boolean {
    let b = (run.amount_analysis || run.loss_analysis_ratio || run.count_analysis) &&
      this.mandatoryCheck(run.amount_analysis, run.amount_analysis_value) &&
      this.mandatoryCheck(run.amount_capped, run.amount_capped_value) && this.isNumber(run.amount_capped_value) &&
      this.mandatoryCheck(run.loss_analysis_ratio, run.loss_analysis_ratio_value) &&
      this.isDecrementValid(run,study) && this.checkRadio(run.attained_age) && this.checkRadio(run.exposure_metric) &&
      this.isRunFilesValid(run)
    return b;
  }


  validDateLowSup(lowDate: date, supDate: date) {
    if (!lowDate || !supDate) return false;
    if (isNaN(supDate.year) || isNaN(supDate.day) || isNaN(supDate.month)) return true
    if (isNaN(lowDate.year) || isNaN(lowDate.day) || isNaN(lowDate.month)) return true

    let low = new Date(lowDate.year + '-' + lowDate.month + '-' + lowDate.day)
    let sup = new Date(supDate.year + '-' + supDate.month + '-' + supDate.day)

    if (!isNaN(low.getTime()) &&
      !isNaN(sup.getTime()) &&
      low <= sup
    ) return true;
    else return false
  }

}
