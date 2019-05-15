import {
  Pipe,
  PipeTransform
} from '@angular/core';

@Pipe({
  name: 'stringLimit'
})
export class StringLimitPipe implements PipeTransform {

  transform(value: string, limit ? : any): any {
    if (!value) return null
    if (!limit) limit = 20
    if (value.length > limit && limit > 3)
      return value.trim().substr(0, limit-3) + "..."
    else
      return value
  }

}
