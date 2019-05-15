import { Component, OnInit } from '@angular/core';
import { DatasetService } from '../../services/dataset.service';
import { Event } from '@angular/router/src/events';

@Component({
  selector: 'app-dataset-list',
  templateUrl: './dataset-list.component.html',
  styleUrls: ['./dataset-list.component.scss']
})
export class DatasetListComponent implements OnInit {

  constructor(public ds:DatasetService) { }

  datasets:any
  query={
    q1:"",
    q2:"",
    q3:"",
    q4:""
  }
  
  ngOnInit() {
   
  }


  datasetToDelete=null
  deletePrompt(e,dataset){
    e.preventDefault()
    this.datasetToDelete = dataset
  }

  confirmDelete(){
    this.deleteDataset(this.datasetToDelete.id,this.datasets.findIndex(d=>d.id==this.datasetToDelete.id))
    this.datasetToDelete=null
  }

  deleteDataset(id,ind){
   
  }

  icon(status){
    switch(status){
      case "ok" : return "check";
      case "bad" : return "exclamation-triangle";
      case "na" : return "times";
      case "load" : return "spinner";
      default: return "exclamation";
    }
  }

  incremental=false
  sortKey="datetime"

  changeSortKey(key){
    if(this.sortKey==key){
      this.changeSortOrder()
    }else{
      this.sortKey=key
      this.resetSortOrder()
    }
  }
  resetSortOrder=()=>{this.incremental=true}
  changeSortOrder=()=>{this.incremental=!this.incremental}
  sortIcon=()=>{ return this.incremental?"sort-down":"sort-up"}
}
