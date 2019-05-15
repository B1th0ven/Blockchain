import {
  Component,
  OnInit,
} from '@angular/core';
import {
  Study
} from '../../shared/models/study';
import { StoreService } from '../../services/store/store.service';
import { UsersService } from '../../services/users.service';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { ToasterService } from '../../../../node_modules/angular5-toaster/dist/angular5-toaster';

@Component({
  selector: 'app-study-list',
  templateUrl: './study-list.component.html',
  styleUrls: ['./study-list.component.scss']
})
export class StudyListComponent implements OnInit  {

  static nbr = 0
  // Holds tab names and types
  tabs: Array < any >
    activeTab: number = 0

  constructor(public store : StoreService,private us:UsersService,private permissionsService: NgxPermissionsService,private tsr: ToasterService) {}

  ngOnInit() {

    StudyListComponent.nbr++;
    this.tabs = new Array < any > ()

    this.tabs.push({
      type: "list",
      name: "Study Management"
    })

    // //LOAD DATA FROM STORE
    // this.store.getStudyTabs().forEach(studyTab => {
    //   this.tabs.push(studyTab)
    // });

    

  }

//   ontabchange(i,t) {
//     //zayda
//     console.log(t)
//     if(t.type != "list")
//     {
//         console.log("calculate permission for study")
//     } else console.log("flush permission and add main role")

//    // console.log("tab changed index ",i," study ",this.tabs[i].study)
//  }

getPermissions(res){     
  this.permissionsService.flushPermissions()
  this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
  this.permissionsService.addPermission(res.primaryRole)
  res.secondaryRoles.forEach(element => {
  this.permissionsService.addPermission(element) });   
  
}

  openTab(i, e) {
    if( this.activeTab != i) {
      e.preventDefault();
      if(this.tabs[i].type != "list" && this.tabs[i].study)
      {
        this.us.getRoleByStID(this.tabs[i].study.stId,this.us.getStoredUser().ruId).subscribe(
        res => {this.getPermissions(res);this.activeTab = i},
        err => this.tsr.pop('error', "Error", "Server Error")
      )}else if(this.tabs[i].type == "create") {
        this.getPermissions({primaryRole:"PRODUCER",secondaryRoles:["STUDY_READER","STUDY_WRITER","STUDY_CANCELLER",
          "DATASET_CREATOR","DATASET_EDITOR", "DATASET_CONTROLS_EXECUTER",
          "RUN_CREATOR","RUN_EDITOR","RUN_IBNR_UPLOADER","RUN_IBNR_READER","RUN_LAUNCHER","RUN_DELETER"]})
        this.activeTab = i
      } else {
      
      this.activeTab = i
    
      }
  }
  }

  closeTab(i, e ? ) {
    if (e)
      e.preventDefault();

    if(this.activeTab == i)
    {
      if( i > 1 && this.tabs[i-1].study)
      this.us.getRoleByStID(this.tabs[i-1].study.stId,this.us.getStoredUser().ruId).subscribe(
        res => {console.log(res);
          this.getPermissions(res);
          this.activeTab--
        },
        err => this.tsr.pop('error', "Error", "Server Error")
      )
      else this.activeTab--
    }

    if (this.activeTab > i && this.activeTab > 0 && this.tabs.length)
      this.activeTab--;
    this.tabs.splice(i, 1);

   // this.store.setStudyTabs(this.tabs.slice(1))
    console.log("calculate permission for study,checktype")
  }

  newTab(event) {

    let tab = {
      type: "",
      name: "",
      study: null,
    }
    tab.type = event.type;
    switch (event.type) {
      case "list":
        tab.name = "Study Management";
        break;
      case "create":
        tab.name = "New Study Creation";
        this.getPermissions({primaryRole:"PRODUCER",secondaryRoles:[]})
        break;
      case "modify":
        let ind = this.tabs.findIndex(t => {
          if (!t.study) return false;
          return t.study["stId"] == event.param["stId"]
        })

        this.us.getRoleByStID(event.param["stId"],this.us.getStoredUser().ruId).subscribe(
          res => {console.log(res);this.getPermissions(res);
            if(this.permissionsService.getPermission("STUDY_READER")){
            if (ind >= 0) {         

              if (event.open)
                this.activeTab = ind
            } else {       
    
              tab.name = event.param["stCode"];
              tab.study = event.param;
              this.tabs.push(tab);
              if (event.open)
                this.activeTab = this.tabs.length - 1;
            }
          } else this.tsr.pop('warning', "Warning", "Access denied")
          },
          err => this.tsr.pop('error', "Error", "Server Error")
        )              
        break;


        // if (ind >= 0) {         

        //   if (event.open)
        //     this.activeTab = ind
        // } else {       

        //   tab.name = event.param["stCode"];
        //   tab.study = event.param;
        //   this.tabs.push(tab);
        //   if (event.open)
        //     this.activeTab = this.tabs.length - 1;
        //   break
        // }
    }

    if (event.type != "modify") {
      this.tabs.push(tab);
      if (event.open)
        this.activeTab = this.tabs.length - 1;
    }

    //ADD TO STORE
    this.store.setStudyTabs(this.tabs.slice(1))
  }

  onSave(study, tab) {

    this.closeTab(tab)
    //this.activeTab = 0
  }

  onContinue(study: Study, tab) {
    //
    // this.tabs[tab].study = Study.mapApiStudy(study)
    this.tabs[tab].name = study.code
    //this.closeTab(tab)
  }

  onStatusChange(e,index){
    this.tabs[index].study.status = e
  }

}
