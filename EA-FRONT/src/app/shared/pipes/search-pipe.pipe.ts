import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'searchPipe'
})
export class SearchPipePipe implements PipeTransform {

  public transform(value , term: string) {
    
        if (!term) return value;
        return (value || []).filter( (item) => String(item).toUpperCase().match(term.toLocaleUpperCase()));
      }

}
