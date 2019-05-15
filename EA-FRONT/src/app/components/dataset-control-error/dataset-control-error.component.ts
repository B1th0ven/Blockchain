import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { Control } from '../../shared/models/control';
import { DatasetDesignService } from '../../services/dataset-design.service';
import { DatasetControlLogicService } from '../../services/dataset-control-logic.service';
import { Dataset } from '../../shared/models/dataset';
declare let $: any;
@Component({
  selector: '[app-dataset-control-error]',
  templateUrl: './dataset-control-error.component.html',
  styleUrls: ['./dataset-control-error.component.scss']
})
export class DatasetControlErrorComponent implements OnInit, OnChanges {
  errorType = 5;
  @Input() control: Control;
  @Input() tabNbr: number;
  @Input() i: number;
  @Input() showobject: any;
  @Input() study;
  @Input() dataset: Dataset ;
  @Input() productHeader;
  @Input() policyHeader;
  @Input() controls;
  map : Map<number,Array<String>> = new Map
  
  isOpen = false ;
  constructor(private ds:DatasetDesignService, private ls: DatasetControlLogicService ) { }
  fillingtype() {
        if(this.control.errors>0 ){
          if(this.control.identifier =='formatControl' || this.control.identifier =='productFormatControl' ){
            this.errorType = 3 ;
          }
          else if(this.control.identifier == 'Missing Values Check' || this.control.identifier == 'Missing Values Check_2' || this.control.identifier == 'Missing Values Check Blocking' || this.control.identifier == 'Product file information should match study metadata' || this.control.identifier == 'Incidence_Death XOR Incidence/Death'){
            this.errorType = 0
          }else if(this.control.identifier != 'Missing Values Check' && this.control.identifier != 'Missing Values Check_2' && this.control.identifier != 'Missing Values Check Blocking' && this.control.identifier != 'Product file information should match study metadata' && this.control.identifier != 'Incidence_Death XOR Incidence/Death') {
            this.errorType = 1
          }
        }
        // if ( this.control.errors > 0  && (this.control.identifier == 'Missing Values Check' || this.control.identifier == 'Missing Values Check_2' || this.control.identifier == 'Missing Values Check Blocking' || this.control.identifier == 'Product file information should match study metadata' || this.control.identifier == 'Incidence_Death XOR Incidence/Death') ){
        //   this.errorType = 0; // variablecolumn
        // } else if (this.control.errors > 0  && this.control.identifier != 'Missing Values Check' && this.control.identifier != 'Missing Values Check_2' && this.control.identifier != 'Missing Values Check Blocking' && this.control.identifier != 'Product file information should match study metadata' && this.control.identifier != 'Incidence_Death XOR Incidence/Death'){
        //   this.errorType = 1; // line }
         else if (this.control.notExecuted) {
          this.errorType = 2; } // notexecuted 
        // else if (this.control.name.includes('Format') && this.control.errors > 0) {
        //   this.errorType = 3; // ColumnName
        // }
      }
  ngOnInit() {
   
  }
  show() {
    if(this.errorType !=5){
    if (this.showobject) {
   $('#collapsefunc-' + this.i + '-' + this.tabNbr).collapse('show');
   this.isOpen ? this.isOpen = false : this.isOpen = true;
     } else if (!this.showobject) {
      $('#collapsefunc-' + this.i + '-' + this.tabNbr).collapse('hide');
     this.isOpen ? this.isOpen = false : this.isOpen = true;
     }
     }
  }
  ngOnChanges() {
    if(!this.control.running){
    this.fillingtype();
    this.show();
    console.log(this.control.affectedColumns);
  }
  }
}
