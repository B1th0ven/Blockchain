import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'undersoreToSpace'
})
export class UndersoreToSpacePipe implements PipeTransform {

  transform(value: String): any {
    return value.split("_").join(" ");
  }

}
