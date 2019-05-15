import {
  Pipe,
  PipeTransform
} from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

  transform(date: Date, args ? : any): any {
    if (!date) return "N/A"
    if (isNaN(date.getTime())) return "NOT A VALID DATE"
    else {
      var dds = date.getDate();
      var mms = date.getMonth() + 1; //January is 0!
      let dd
      let mm

      var yyyy = date.getFullYear();
      if (dds < 10) {
        dd = '0' + dds;
      }
      else dd = dds
      if (mms < 10) {
        mm = '0' + mms;
      }else mm = mms
      return dd + '/' + mm + '/' + yyyy;
    }
  }

}
