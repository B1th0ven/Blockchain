import { Component, OnInit, Input } from '@angular/core';
import { SCREEN, HoverOverService } from '../hover-over/hover-over.service';
import { UsersService } from '../../services/users.service';
import { User } from '../../shared/models/user';
import { Study } from '../../shared/models/study';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { ToasterService } from '../../../../node_modules/angular5-toaster/dist/angular5-toaster';

@Component({
  selector: 'app-study-access-right',
  templateUrl: './study-access-right.component.html',
  styleUrls: ['./study-access-right.component.scss']
})
export class StudyAccessRightComponent implements OnInit {
  @Input() study:Study
  @Input()editable:boolean

  readonly screen = SCREEN.ACCESS_RIGHT

  constructor(private us:UsersService,private permissionsService: NgxPermissionsService,private tsr: ToasterService,public hs:HoverOverService) { }

  loading = false
  loaded  = false
  users:any[] = []
  allUsers:any[] = []
  selecteduser:any
  pager:any
  query:any = {user:"",gp:"",func:"",scope:"",scopetype:"",sp:"",ex:""}
  sortkey:any
  ascOrdesc:number = 1


  studyProfiles=[
    {key:1,value:"Producer"},
    {key:2,value:"Private Client"},
    {key:3,value:"Reviewer"},
    {key:4,value:"Other"},
  ]

  dropdownSettings:any = {
    singleSelection: false,
    idField: 'itemName',
    textField: 'itemValue',
    selectAllText: 'All',
    unSelectAllText: 'None',
    itemsShowLimit: 0,
    allowSearchFilter: false
  };

values = [{itemName:1,itemValue:"true"},{itemName:2,itemValue:"false"}]
selectedValues = [{itemName:1,itemValue:"true"},{itemName:2,itemValue:"false"}]

  ngOnInit() {
    this.pager = {selectedpage:1,pagesize:10,totalOfPages:0}    
    this.loading = true;
    this.us.getStudyRolesException(this.study.id).subscribe(
      res => {
        res.forEach(element => {
        if(element.ureRuId.ruId != this.us.getStoredUser().ruId )
        this.allUsers.push({user:new User(element.ureRuId),role:element.ureRole.trim().toLocaleLowerCase(),defaultRole:element.ureDefaultRole.trim().toLocaleLowerCase(),isDefault:element.default})
        this.pager.totalOfPages = Math.ceil(this.allUsers.length / this.pager.pagesize) 
        this.allUsers = this.transform(this.allUsers)
        this.users = this.allUsers
          this.paginate()
         // console.log("show users!!",this.allUsers)
      }); 
      this.loading = false;

    },
    err => {
      console.log(err)
      this.loading = false;

    })


  }

  onChange(e){
    console.log(e)
  }

  onRoleChange(e,u){
    console.log(u)
    console.log(e);
    
    console.log(this.allUsers);
    
    let index = this.allUsers.indexOf(u)
    console.log(index);

    this.allUsers[index].user.isChanged = true
    this.allUsers[index].role = e
    if(this.allUsers[index].role == this.allUsers[index].defaultRole) {
      this.allUsers[index].isDefault = true
    } else {
      this.allUsers[index].isDefault = false
    }
  }

  updateException(u){
    u.user.saveLoader = true
    u.user.isChanged = false
    this.us.addOrUpdateException(this.study.id,u.user.id,u.role).subscribe(
      res => {
        if(this.us.getStoredUser().ruId == u.user.id){
        this.us.getRoleByStID(this.study.id,this.us.getStoredUser().ruId).subscribe(
          res => {this.getPermissions(res); u.user.saveLoader = false},
          err => this.tsr.pop('error', "Error", "Server Error")
        )} else  u.user.saveLoader = false;
        this.tsr.pop('success', "Success", "User Exception has been saved")
       
      },
      err => { console.log(err);this.tsr.pop('error', "Error", "Server Error");u.user.saveLoader=false}
    )
  }

  isSaveLoading(){
    this.allUsers.forEach(e => {
      if(e.user.isChanged)
      {
        if(e.user.saveLoader)
        return true
      }
    });
    return false
  }
  
  getPermissions(res){     
    this.permissionsService.flushPermissions()
    this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
    this.permissionsService.addPermission(res.primaryRole)
    res.secondaryRoles.forEach(element => {
    this.permissionsService.addPermission(element) });   
    
  }

  // addException(){
  //   if(this.allUsers.length == 0){
  //   this.us.getAllUsers().subscribe(
  //     res => {
  //         res.forEach(e => {
  //           if(e.ruId != this.us.getStoredUser().ruId && !this.users.find(function (obj) { return obj.user.id == e.ruId }) )
  //           this.allUsers.push(new User(e))

  //         });
  //         this.users.push({user:new User(),role:""})

  //     },
  //     err => {console.log(err);this.tsr.pop('error', "Error", "Server Error")}
  //   ) } else this.users.push({user:new User(),role:""})
  // }

  // newExcep(e,i){
  //   let user = this.allUsers.find(u => u.id == this.selecteduser)
  //   this.users[i].user = user
  // } 

  saveChanges(){
    let res = this.allUsers.filter(s => s.user.isChanged === true)
    if(res && res.length > 0)
    res.forEach(u => this.updateException(u))
    else this.tsr.pop('info', "Information", "All changes are already saved")

  }

  paginate() {
    let tab = this.search()
    let val = (this.pager.selectedpage - 1) * this.pager.pagesize;
    this.pager.totalOfPages =  Math.ceil(tab.length / this.pager.pagesize)
    this.users = this.search().slice(val, val + this.pager.pagesize)
         
  }

 

  onSelectPage(page){this.pager.selectedpage=page;this.paginate();}
  onSelectPageSize(size){this.pager.pagesize=parseInt(size);this.pager.selectedpage=1;this.paginate();}

  typingTimeout = null;
  filter = (e?) => {
    clearTimeout(this.typingTimeout)
    this.typingTimeout = setTimeout(() => {
      this.filtrer(e)
    }, 400);
  }

   contains(s,p) {
    return s.replace("_"," ").toLocaleLowerCase().trim().includes(p.toLocaleLowerCase().trim())
  }

  filtrer(e?){
    console.log(e,this.selectedValues.length < 2)
    if(this.selectedValues.length < 2)
    this.query.ex = this.selectedValues[0].itemValue
    else this.query.ex = ""
    this.pager.selectedpage = 1 
    this.paginate()
  }
 
  transform(arr){
    let res_producer = []
    let res_reviewer = []
    let res_other = []

    for (let e of arr)
    {
      if (e.role.toLocaleLowerCase().trim() == "producer")
      res_producer.push(e)
      else if (e.role.toLocaleLowerCase().trim() == "reviewer")
      res_reviewer.push(e)
      else
      res_other.push(e)
    }
    res_producer = res_producer.sort((a,b) =>(a.user.name +" "+a.user.lastname > b.user.name +" "+b.user.lastname? 1 : -1))
    res_reviewer = res_reviewer.sort((a,b) =>(a.user.name +" "+a.user.lastname > b.user.name +" "+b.user.lastname? 1 : -1))
    res_other = res_other.sort((a,b) =>(a.user.name +" "+a.user.lastname > b.user.name +" "+b.user.lastname? 1 : -1))
    return [...res_producer,...res_reviewer,...res_other];
  }
  
  search(){
     let res = this.allUsers
     for (let key of Object.keys(this.query)) {
      if(this.query[key] != "")
       {
        switch(key) { 
          case "user": { 
            res =  res.filter(s => this.contains(s.user.name + s.user.lastname,this.query[key])) 
             break; 
          } 
          case "gp": { 
            res =  res.filter(s => this.contains(s.user.role,this.query[key])) 
            break; 
         } 
         case "func": { 
          res =  res.filter(s => this.contains(s.user.function,this.query[key])) 
          break; 
         } 
         case "scope": { 
          res =  res.filter(s => this.contains(s.user.scope.rsName,this.query[key])) 
          break; 
         } 
         case "scopetype": { 
          res =  res.filter(s => this.contains(s.user.scope.rsType,this.query[key])) 
          break; 
         } 
         case "sp": {           
          res =  res.filter(s => this.contains(s.role,this.query[key])) 
          break; 
         } 
         case "ex":
         res =  res.filter(s => String(s.isDefault) != this.query[key]) 
    
          break;
           default: { 
             break; 
          } 
         } 
       }
    }


    return res
  }

  sort(e){
    switch(e) { 
      case "user": { 
         if(this.sortkey && this.sortkey == "user")
           this.ascOrdesc = -this.ascOrdesc
         else {this.ascOrdesc = 1,this.sortkey = "user"}
         this.allUsers = this.allUsers.sort((a,b) =>(a.user.name +" "+a.user.lastname > b.user.name +" "+b.user.lastname? this.ascOrdesc : -this.ascOrdesc))
         break; 
      } 
      case "gp": { 
        if(this.sortkey && this.sortkey == "gp")
           this.ascOrdesc = -this.ascOrdesc
         else {this.ascOrdesc = 1,this.sortkey = "gp"}
         this.allUsers = this.allUsers.sort((a,b) =>(a.user.role > b.user.role ? this.ascOrdesc : -this.ascOrdesc))         
        break; 
     } 
     case "func": { 
      if(this.sortkey && this.sortkey == "func")
      this.ascOrdesc = -this.ascOrdesc
      else {this.ascOrdesc = 1,this.sortkey = "func"}
    this.allUsers = this.allUsers.sort((a,b) =>(a.user.function > b.user.function? this.ascOrdesc : -this.ascOrdesc))
      break; 
     } 
     case "scope": { 
      if(this.sortkey && this.sortkey == "scope")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "scope"}
    this.allUsers = this.allUsers.sort((a,b) =>(a.user.scope.rsName > b.user.scope.rsName? this.ascOrdesc : -this.ascOrdesc))    
      break; 
     } 
     case "scopetype": { 
      if(this.sortkey && this.sortkey == "scopetype")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "scopetype"}
    this.allUsers = this.allUsers.sort((a,b) =>(a.user.scope.rsType > b.user.scope.rsType? this.ascOrdesc : -this.ascOrdesc))  
      break; 
     } 
     case "sp": {    
      if(this.sortkey && this.sortkey == "sp")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "sp"}
    this.allUsers = this.allUsers.sort((a,b) =>(a.role > b.role? this.ascOrdesc : -this.ascOrdesc))           
      break; 
     } 
       default: { 
         break; 
      } 
     }
     this.pager.selectedpage = 1
     this.paginate()
  }

}
