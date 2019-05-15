import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'timespan'
})
export class TimespanPipe implements PipeTransform {

  transform(totalSeconds: any): any {

    let hours = Math.floor(totalSeconds / 3600);
    totalSeconds %= 3600;
    let minutes = Math.floor(totalSeconds / 60);
    let seconds = totalSeconds % 60;

    let timespan = ""

    if (minutes>1) timespan += minutes + " minutes "
    else if (minutes==1) timespan += minutes + " minute "

    if (hours>1) timespan = hours + " hours " + timespan
    else if (hours==1) timespan = hours + " hour " + timespan
    else{
      if (seconds!=1) timespan += seconds + " seconds"
      else if (seconds==1) timespan += seconds + " second"
    }

    return timespan

  }

}
