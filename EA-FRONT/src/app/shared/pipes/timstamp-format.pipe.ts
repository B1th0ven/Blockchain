import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timstampFormat'
})
export class TimstampFormatPipe implements PipeTransform {

  transform(date: Date, args ? : any): any {
    if (!date) return "N/A"
    if (isNaN(date.getTime())) return "NOT A VALID DATE"
    else {
      var dds = date.getDate();
      var mms = date.getMonth() + 1; //January is 0!
      var hh = date.getHours()
      var MM = date.getMinutes()
      var ss = date.getSeconds()
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
      return dd + '/' + mm + '/' + yyyy + " " + hh + ":" + MM +":"+ ss;
    }
  }

}
