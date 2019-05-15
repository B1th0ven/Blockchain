import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
  sm=false
  tl=false
  ad=false
  log=false

  enterOrleave(arg,val){
    switch(arg) { 
      case 'sm': { 
         this.sm = val
         break; 
      } 
      case 'tl': { 
         this.tl = val 
         break; 
      } 
      case 'ad': { 
        this.ad = val
        break; 
     } 
     case 'log': { 
      this.log = val 
      break; 
   } 
      default: { 
         break; 
      } 
   } 
  }
  
  

}
