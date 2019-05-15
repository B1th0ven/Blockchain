import { Pipe, PipeTransform } from '@angular/core';
import { Run } from '../models/run';
import { Decrement } from '../models/decrement';

@Pipe({
  name: 'decrementsFilter'
})
export class DecrementsFilterPipe implements PipeTransform {

  transform(decrements: string[], run: Run,dec:string): any {
    return (decrements)? decrements.filter(decrement=> run && run.decrements && ( decrement == dec || decrement != dec && run.decrements.findIndex(d=>d.decrement == decrement) < 0 ) ):null;
  }

}
