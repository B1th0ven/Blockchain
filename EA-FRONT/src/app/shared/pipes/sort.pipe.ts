import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sort',
  pure:false
})
export class SortPipe implements PipeTransform {

  transform(items: Array<any>, key: any , incremantal: boolean ): any {
    if ( !items || !Array.isArray(items)) return items
    else{
      if(incremantal){
        items.sort((a,b)=>{
          if ( String(a[key]).toLowerCase() >= String(b[key]).toLowerCase() ) return 1
          else return -1
        })
      }else{
        items.sort((a,b)=>{
          if (String(a[key]).toLowerCase() <= String(b[key]).toLowerCase()) return 1
          else return -1
        })
      }
      return items
    }
  }

}
