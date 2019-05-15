import {
  Component,
  OnInit
} from '@angular/core';
import {
  Tab
} from '../../models/tab.class';
import {
  TAB_TYPE
} from '../../enums/tab.enum';
import {
  ExpectedTable
} from '../../models/expected-table.model';
import {
  TablesService
} from '../../services/tables.service';
import { UsersService } from '../../../../services/users.service';
import { Router } from '../../../../../../node_modules/@angular/router';
import { NgxPermissionsService } from '../../../../../../node_modules/ngx-permissions';
import { ToasterService } from '../../../../../../node_modules/angular5-toaster/dist/angular5-toaster';

@Component({
  selector: 'app-tabs-manager',
  templateUrl: './tabs-manager.component.html',
  styleUrls: ['./tabs-manager.component.scss']
})
export class TabsManagerComponent implements OnInit {

  tabs: Tab[]

  activeTab = 0

  constructor(private ets: TablesService,private us: UsersService, private router: Router,private permissionsService: NgxPermissionsService,private tsr: ToasterService) {}

  ngOnInit() {
    let  perm = [];
 
    if ( !this.us.getStoredUser() )
    {
      this.router.navigate(['login'])
    } else {
      this.us.user.subscribe(user=>
        {
          if(user)
          {
            perm.push(user.ruRole.toUpperCase().trim())
            this.permissionsService.loadPermissions(perm)
          }
        
        }
      )
    }
    console.log("tabs tabs atabs everywhere table lib")
    this.tabs = new Array < Tab > ()
    this.tabs.push({
      name: "Table Library",
      type: TAB_TYPE.SHOW_LIST,
      entity: null
    })
  }

  getPermissions(res){     
    this.permissionsService.flushPermissions()
    this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
    this.permissionsService.addPermission(res.primaryRole)
    res.secondaryRoles.forEach(element => {
    this.permissionsService.addPermission(element) });   
    
  }

  openTab(i, event ? : Event) {
    console.log(this.tabs[i]);
    
    if( this.activeTab != i) {
      if (event) event.preventDefault();
      if(this.tabs[i].name != "Table Library" && this.tabs[i].entity && this.tabs[i].entity.id )
      {
         this.us.getRoleByExpID(this.tabs[i].entity.id,this.us.getStoredUser().ruId).subscribe(
         res => {console.log("table roles",res,this.tabs[i].entity.id);
         this.getPermissions(res);this.activeTab = i},
         err => this.tsr.pop('error', "Error", "Server Error")
       )
 
      this.activeTab = i

    } else {
      this.permissionsService.flushPermissions()
     this.permissionsService.addPermission(this.us.getStoredUser().ruRole)      
     this.permissionsService.addPermission('TABLE_WRITER')      

      this.activeTab = i
    
      }
  }
    // console.log('check tab tqb',this.tabs[i])
    // if (event) event.preventDefault()
    // this.activeTab = i;
  }

  newTab(event) {
    //console.log("new new new new")
    if (event.entity && event.entity.id && this.tabs.find(t => t.entity && t.entity.id == event.entity.id)) {
      let tabIndex = this.tabs.findIndex(t => t.entity && t.entity.id == event.entity.id)
      // if (event.open){
      //   this.activeTab = tabIndex       
      // }


      this.us.getRoleByExpID(event.entity.id,this.us.getStoredUser().ruId).subscribe(
        res => {
          this.getPermissions(res);
            if(this.permissionsService.getPermission("TABLE_READER")){
              this.tabs.push({
                name: (event.entity) ? event.entity.name : "Tab Name",
                type: TAB_TYPE.CREATE,
                entity: event.entity
              })
              if (event.open)
                this.activeTab = tabIndex
          } else this.tsr.pop('warning', "Warning", "Access denied")
        },
        err => this.tsr.pop('error', "Error", "Server Error")
      )


    } else {
     
      if(event.type == TAB_TYPE.MODIFY){

      this.us.getRoleByExpID(event.entity.id,this.us.getStoredUser().ruId).subscribe(
        res => {
          this.getPermissions(res);
            if(this.permissionsService.getPermission("TABLE_READER")){
              this.tabs.push({
                name: (event.entity) ? event.entity.name : "Tab Name",
                type: TAB_TYPE.CREATE,
                entity: event.entity
              })
              if (event.open)
                this.activeTab = this.tabs.length - 1
          } else this.tsr.pop('warning', "Warning", "Access denied")
        },
        err => this.tsr.pop('error', "Error", "Server Error")
      )
      
    } else {

      this.permissionsService.flushPermissions()
      this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
      this.permissionsService.addPermission('TABLE_WRITER')
      this.permissionsService.addPermission('TABLE_STATUS')


      this.tabs.push({
        name: (event.entity) ? event.entity.name : "Tab Name",
        type: TAB_TYPE.CREATE,
        entity: event.entity
      })

      this.activeTab = this.tabs.length - 1


    }
    
    }  

  }

  closeTab(i, e ? ) {
    if (e)
      e.preventDefault();

    if (this.activeTab >= i && this.activeTab > 0 && this.tabs.length)
      this.activeTab--;
    this.tabs.splice(i, 1);
  }

  onSave(event, tab, i ? ) {
    tab.entity = event.table
    tab.name = (event.table) ? event.table.name : "ERROR";

    let pCode = this.getPrefix(event.previousCode)
    let nCode = this.getPrefix(event.table.code)

    this.tabs.forEach(ti => {

      if (ti.entity && ti.entity.id && ti.entity.id != event.table.id) {
        let code = this.getPrefix(ti.entity.code)
        if (code == pCode || code == nCode)
          this.ets.getOneTable(ti.entity.id).subscribe((res: ExpectedTable) => {
            ti.entity.latest_version = res.latest_version
          })
      }
    });

    if (i) {
      this.closeTab(i)
      this.openTab(0)
    }

  }

  onDelete(i) {
    this.closeTab(i)
  }

  private getPrefix(code: any) {
    let p = String(code).split("_").slice(0, -1).join("_");
    return p
  }
}
