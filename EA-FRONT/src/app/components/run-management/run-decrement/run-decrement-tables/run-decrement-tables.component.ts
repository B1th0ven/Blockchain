import { Component, OnInit, Input, EventEmitter, Output, QueryList, ViewChildren } from '@angular/core';
import { Run } from '../../../../shared/models/run';
import { RunTable } from '../../../../shared/models/run-table';
import { Decrement } from '../../../../shared/models/decrement';
import { ExpTabRun } from '../../../../shared/models/expected-tab-run';
import { Study } from '../../../../shared/models/study';
import { RunDecTabDefComponent } from '../run-dec-tab-def/run-dec-tab-def.component';

@Component({
  selector: 'app-run-decrement-tables',
  templateUrl: './run-decrement-tables.component.html',
  styleUrls: ['./run-decrement-tables.component.scss']
})
export class RunDecrementTablesComponent implements OnInit {

  @Input() decrement:Decrement
  @Input() tab:number
  @Input() editable:boolean
  @Input() study:Study
  @Input() run:Run
  @Output() close: EventEmitter < any > = new EventEmitter < any > ()
  @Output() save: EventEmitter < any > = new EventEmitter < any > ()


  @ViewChildren(RunDecTabDefComponent) childComps: QueryList<RunDecTabDefComponent>;



  tables: ExpTabRun[] = []
  component_number:number
  runDsID: string 
  cansave: boolean = false;


  constructor() { }

  ngOnInit() {
    this.runDsID = this.study.id+"/"+this.run.id
    this.component_number  = RunDecrementTablesComponent.NUMBER++;

    this.decrement.ExpectedTable.forEach(s => this.tables.push(s))
    if (!this.tables || this.tables.length == 0)
      this.tables = new Array<ExpTabRun>()

      if (!this.decrement.ExpectedTable ||  this.decrement.ExpectedTable.length == 0)
      this.decrement.ExpectedTable = new Array<ExpTabRun>()



      console.log("--------------tabdede",this.tables)
  }

  createTable(){
    //this.tables.push(new ExpTabRun())
    this.decrement.ExpectedTable.push(new ExpTabRun())
  }

  deleteTable(ind)
  {
    //this.tables.splice(ind,1)
    this.decrement.ExpectedTable.splice(ind,1)
  }

  canCreateBasis(){
    //return this.tables.length < 10
    return this.decrement.ExpectedTable.length < 10
  }

  saveBasis(){
    // if(this.childComps)
    // this.childComps.forEach(s => s ? s.save():null)
    // this.decrement.ExpectedTable =  []
    // this.tables.forEach(t => this.decrement.ExpectedTable.push(t))
     this.save.emit()
     this.close.emit()

  }

 
  canIsavePlease(){
    if (!this.decrement.ExpectedTable || this.decrement.ExpectedTable.length == 0)
    return false 

    for(let t of this.decrement.ExpectedTable){
    if(t.cansaveexp != undefined && !t.cansaveexp)
    return true
    if(t.cansaveexpadj != undefined && !t.cansaveexpadj)
    return true
    if(t.cansaveexptrend != undefined && !t.cansaveexptrend)
    return true
    }
    return false
  }

  cancel(){
    this.decrement.ExpectedTable = new Array<ExpTabRun>()
    this.tables.forEach(s => this.decrement.ExpectedTable.push(s))
    this.tables = new Array<ExpTabRun>()
    this.close.emit()
  }

  static NUMBER = 0

}
