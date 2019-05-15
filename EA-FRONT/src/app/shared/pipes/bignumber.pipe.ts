import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'bignumber'
})
export class BignumberPipe implements PipeTransform {

  transform(value: String, args?: any): any {
    if ( Number(value) == NaN ) return value
    
    value= String(value)

    if (value.length > 9){
      value = value.substring(value.length-9,0) + " billion"
    }else if (value.length > 6){
      value = value.substring(value.length-6,0) + " million"
    }else if (value.length > 3){
      value = value.substring(value.length-3,0) + "k"
    }

    return value;
  }

}
