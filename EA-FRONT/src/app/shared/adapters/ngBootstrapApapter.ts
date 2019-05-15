import {
  NgbDateAdapter,
  NgbDateStruct
} from '@ng-bootstrap/ng-bootstrap';
import {
  Injectable
} from '@angular/core';
import {
  date
} from '../models/date';


@Injectable()
export class NgbDateNativeAdapter extends NgbDateAdapter < date > {
  fromModel(indate: date): NgbDateStruct {
    if (!indate) return null
    if (isNaN(indate.year) || isNaN(indate.day) || isNaN(indate.month)) return null;
    return ( indate && indate.valid() ) ? {
      year: indate.year,
      month: indate.month,
      day: indate.day
    } : null;
  }

  toModel(indate: NgbDateStruct): date {
    return indate ? new date(indate.year, indate.month, indate.day) : null;
  }
}
