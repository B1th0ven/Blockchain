import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filesFilter',
  pure:false
})
export class FilesFilterPipe implements PipeTransform {

  transform(items: any, query: any): any {
    if(query == null || query == "" ) return items

    return items.filter(file=>{
      if ( !file.name ) return false;
  
        if ( new RegExp(query, 'gi').test(file.name)){
          return true
        }
      
      return false
    })
  }

}
