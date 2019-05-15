import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appAutoSlash]'
})
export class AutoSlashDirective {

  constructor(private _elementRef : ElementRef) { }

  @HostListener('keyup', ['$event']) onKeyUp(keyEvent:KeyboardEvent) {
    let value : string = this._elementRef.nativeElement.value
    let key = keyEvent.key
    if ( value && key != "Backspace" ) {
      value = String(value).replace(/\//g,"");
      let tab = value.split("")
      if (tab.length >= 2 )
        tab.splice(2, 0, "/")
      if (tab.length >= 5 )
        tab.splice(5, 0, "/")
      this._elementRef.nativeElement.value = tab.join("")
    }
  }

}
