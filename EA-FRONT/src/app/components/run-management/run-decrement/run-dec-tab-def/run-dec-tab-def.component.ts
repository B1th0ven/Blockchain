import { Component, OnInit, Input, Output,EventEmitter } from '@angular/core';
import { Decrement } from '../../../../shared/models/decrement';
import { RunService } from '../../../../services/run.service';
import { ExpTabRun } from '../../../../shared/models/expected-tab-run';
import { ToasterService } from '../../../../../../node_modules/angular5-toaster/dist/angular5-toaster';
import { UsersService } from '../../../../services/users.service';
import { Run } from '../../../../shared/models/run';

@Component({
  selector: 'app-run-dec-tab-def',
  templateUrl: './run-dec-tab-def.component.html',
  styleUrls: ['./run-dec-tab-def.component.scss']
})
export class RunDecTabDefComponent implements OnInit {
  @Input() ExpTabRun:ExpTabRun
  @Input() DecType:string
  @Input() editable:boolean
  @Input() runDsId:string
  @Input() run : Run
  ExprReport: any[] = []
  CompNo: number
  base: any[] = []
  trend: any[] = []
  adjs: any[] = []
  baseLoading:boolean
  adjLoading:boolean
  trendLoading:boolean
  baseCheckLoading:boolean
  adjCheckLoading:boolean
  trendCheckLoading:boolean
  dsid:any;
  rid:any;
  selectedBase:any[] = []
  selectedTrend: any[] = [];
  selectedAdj: any[] = [];

  dropdownSettings:any
  selected:any[] = []
  itemList:any[]= [];
  constructor(private rs : RunService, private tsr: ToasterService, private userService : UsersService) { 
  }

  ngOnInit() {
    this.dsid = this.runDsId.split("/")[0]
    this.rid =  this.runDsId.split("/")[1]
    this.initTables()
    this.CompNo = RunDecTabDefComponent.NUMBER++

    if(this.ExpTabRun.id_base){
      this.selectedBase[0] = {"id":this.ExpTabRun.id_base};
    }

    if(this.ExpTabRun.id_adjustment)
    {
      this.selectedAdj[0] = {"id":this.ExpTabRun.id_adjustment};
      this.onExpChange(this.selectedAdj[0].id,"adj")
    }

    if(this.ExpTabRun.id_trend){
      this.selectedTrend[0] = {"id":this.ExpTabRun.id_trend};
      this.onExpChange(this.selectedTrend[0].id,"trend")
    }

    console.log("base ---->",this.ExpTabRun.ExprReportBase,"trend ----->",this.ExpTabRun.ExprReporTTrend,"adj ------->",this.ExpTabRun.ExprReportAdj)

    

    this.dropdownSettings = {
      singleSelection: true,
      text: "",
      enableSearchFilter: true,
      classes: "myclass custom-class",
      badgeShowLimit: 0,
      disabled:!this.editable
    };


    
  }


  initTables () {
    this.baseLoading = true
    this.trendLoading = true
    this.adjLoading = true
    this.rs.getExpectedTabByTypeAndDecrement("base",this.DecType,this.userService.getStoredUser().ruId).subscribe(
      res => {
        //console.log("exp check",res)
        for (let obj of res[0].content){
          let s = {"id":obj.retId,"itemName":obj.retCode, "status": obj.retStatus, "version": obj.retLatestVersion }
          this.base.push(s)
        }

        if(this.ExpTabRun.id_base) {
          let o = this.base.find(obj => obj.id == this.ExpTabRun.id_base)
          this.selectedBase[0].itemName = o.itemName
          this.selectedBase[0].status = o.status
          this.selectedBase[0].version = o.version
          this.onExpChange(this.selectedBase[0].id,"base")
        }

        this.baseLoading = false
      },
      err => {
        this.tsr.pop("error", "Error", "Server Error")
        console.log(err)
        this.baseLoading = false
      }
    )

    this.rs.getExpectedTabByTypeAndDecrement("trend",this.DecType,this.userService.getStoredUser().ruId).subscribe(
      res => {
        for (let obj of res[0].content){
          let s = {"id":obj.retId,"itemName":obj.retCode}
          this.trend.push(s)
        }

        if(this.ExpTabRun.id_trend) {
          let o = this.trend.find(obj => obj.id == this.ExpTabRun.id_trend)
          this.selectedTrend[0].itemName = o.itemName
          this.onExpChange(this.selectedTrend[0].id,"trend")
        }
        this.trendLoading = false
      },
      err => {
        this.tsr.pop("error", "Error", "Server Error")
        console.log(err)
        this.trendLoading = false
      }
    )

    this.rs.getExpectedTabByTypeAndDecrement("adjustment",this.DecType,this.userService.getStoredUser().ruId).subscribe(
      res => {
        for (let obj of res[0].content){
          let s = {"id":obj.retId,"itemName":obj.retCode}
          this.adjs.push(s)
        }

        if(this.ExpTabRun.id_adjustment) {
          let o = this.adjs.find(obj => obj.id == this.ExpTabRun.id_adjustment)
          this.selectedAdj[0].itemName = o.itemName
          this.onExpChange(this.selectedAdj[0].id,"adj")
        }
        this.adjLoading = false
      },
      err => {
        this.tsr.pop("error", "Error", "Server Error")
        console.log(err)
        this.adjLoading = false
      }
    )
  }

  getTables(run : Run) {
    let tablesId : Array<number> = []
    run.decrements.forEach(dec => {
      dec.ExpectedTable.forEach(exp => {
        if(exp.id_adjustment) tablesId.push(exp.id_adjustment)
        if(exp.id_base) tablesId.push(exp.id_base)
        if(exp.id_trend) tablesId.push(exp.id_trend)
      })
    })
    console.log("tables ====>",tablesId)
    return tablesId
    
  }

  onExpChange(e,type){
    console.log(e,this.selectedBase)
    console.log(this.run);
    let tablesArray = this.getTables(this.run)
    
    if( e && e != ""){
    switch(type) { 
      case "base": {
        if(this.selectedBase[0]) this.ExpTabRun.id_base = this.selectedBase[0].id
        else this.ExpTabRun.id_base = null
        this.ExpTabRun.cansaveexp = false
        this.ExpTabRun.ExprReportBase = []
        let check = true
        this.baseCheckLoading = true
        if(this.selectedTrend && this.selectedTrend.toString() != "")
            this.onExpChange(this.selectedTrend[0].id,"trend")
        if(this.selectedAdj && this.selectedAdj.toString() != "")
            this.onExpChange(this.selectedAdj[0].id,"adj")
        this.rs.checkBase(this.dsid,this.rid,e,this.run.dimensions, this.run.isDateOfCommencement, this.run.isDateOfBirth, this.run.exposure_metric, tablesArray,this.run.isAgeAtCommencement).subscribe(
          res => 
          {
            this.baseCheckLoading=false;
            this.ExpTabRun.ExprReportBase = res
            for(let rp of this.ExpTabRun.ExprReportBase)
            {
              if(rp.split("/")[1].toLowerCase().trim() == "blocking")
              {
                check = false
                break 
               } 
            }
            this.ExpTabRun.cansaveexp = check            
          },
          err => {
            this.tsr.pop("error", "Error", "Server Error")
            this.baseCheckLoading=false
            console.log(err)
          }
        ) 
         break; 
      } 
      case "trend": { 
        if(this.selectedTrend[0]) this.ExpTabRun.id_trend = this.selectedTrend[0].id
        else this.ExpTabRun.id_trend = null
        this.ExpTabRun.cansaveexptrend = false
        let check = true
        this.trendCheckLoading = true
        this.rs.checkAdjOrTrend(this.selectedBase[0].id,e,"trend",this.run.dimensions, this.run.isDateOfCommencement, this.run.isDateOfBirth,tablesArray,this.run.isAgeAtCommencement).subscribe(
          res =>
          {
            this.ExpTabRun.ExprReporTTrend = []
            this.trendCheckLoading = false
            this.ExpTabRun.ExprReporTTrend = res
            for(let rp of this.ExpTabRun.ExprReporTTrend)
            {
              if(rp.split("/")[1].toLowerCase().trim() == "blocking")
              {
                check = false
                break 
               } 
            }
            this.ExpTabRun.cansaveexptrend = check

          },
          err => {
            this.tsr.pop("error", "Error", "Server Error")
            this.trendCheckLoading=false
            console.log(err)
          }
        )
         break; 
      } 
      case "adj": { 
        if(this.selectedAdj[0]) this.ExpTabRun.id_adjustment = this.selectedAdj[0].id
        else this.ExpTabRun.id_adjustment = null
        this.ExpTabRun.cansaveexpadj = false
        let check = true;
        this.adjCheckLoading = true
        this.rs.checkAdjOrTrend(this.selectedBase[0].id,e,"adjustment",this.run.dimensions, this.run.isDateOfCommencement, this.run.isDateOfBirth,tablesArray,this.run.isAgeAtCommencement).subscribe(
          res =>
          {
            this.ExpTabRun.ExprReportAdj = []
            this.adjCheckLoading = false
            this.ExpTabRun.ExprReportAdj = res
            console.log("adjadj------->",this.ExpTabRun.ExprReportAdj)

            for(let rp of this.ExpTabRun.ExprReportAdj)
            {
              if(rp.split("/")[1].toLowerCase().trim() == "blocking")
              {
                check = false
                break 
               } 
            }
            this.ExpTabRun.cansaveexpadj = check
          },
          err => {
            this.tsr.pop("error", "Error", "Server Error")
            this.adjCheckLoading=false
            console.log(err)
          }
        ) 
        break; 
     } 
      default: { 
         break; 
      } 
   } 
  } else {
       switch(type) {
         case "base": this.ExpTabRun.ExprReportBase = undefined; this.ExpTabRun.id_base = null;this.ExpTabRun.cansaveexp = undefined;this.clear();break;
         case "trend": this.ExpTabRun.ExprReporTTrend = undefined; this.ExpTabRun.id_trend = null;this.ExpTabRun.cansaveexptrend = undefined;break;
         case "adj": this.ExpTabRun.ExprReportAdj = undefined; this.ExpTabRun.id_adjustment = null;this.ExpTabRun.cansaveexpadj = undefined;break;
         default: break;
      }
    }   
  }

  clear(){
    this.onExpChange('','adj')
    this.onExpChange('','trend')
    this.selectedAdj = undefined
    this.selectedTrend = undefined
  }

  save(){
    if( this.selectedBase[0])
    this.ExpTabRun.id_base = this.selectedBase[0].id
    else  this.ExpTabRun.id_base = null
    if(this.selectedAdj[0])
    this.ExpTabRun.id_adjustment = this.selectedAdj[0].id
    else  this.ExpTabRun.id_adjustment = null
    if(this.selectedTrend[0])
    this.ExpTabRun.id_trend = this.selectedTrend[0].id
    else  this.ExpTabRun.id_trend = null
  }

  OnItemDeSelect(item:any){
    console.log(item);
  }
  onSelectAll(items: any){
    console.log(items);
  }
   onDeSelectAll(items: any){
    console.log(items);
  }


  
  static NUMBER = 0;

}
