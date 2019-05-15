import { Component, OnInit, ViewChild } from '@angular/core';
import { of } from '../../../../../../node_modules/rxjs/observable/of';
import { UsersService } from '../../../../services/users.service';
import { User } from '../../../../shared/models/user';
import { UserCreationComponent } from '../user-creation/user-creation.component';
import { ToasterService } from '../../../../../../node_modules/angular5-toaster/dist/angular5-toaster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.scss']
})
export class UsersTableComponent implements OnInit {

  constructor(private us:UsersService, private tsr: ToasterService, private modalService: NgbModal) { }
 
  allusers :any[] = []
  users:any[] = []
  scopes:any[] = []
  loading = false
  saveLoading  = false
  pager:any;
  query:any = {ln:"",fn:"",func:"",scope:"",scopetype:"",role:""}
  sortkey:any
  ascOrdesc:number = 1

  @ViewChild(UserCreationComponent) child:UserCreationComponent
 
  studyProfiles=[
    {key:1,value:"Producer"},
    {key:2,value:"Private Client"},
    {key:3,value:"Reviewer"},
    {key:4,value:"Other"},
  ]
 
  ngOnInit() {
    this.pager = {selectedpage:1,pagesize:20,totalOfPages:0}    
    this.getUsers()
    this.getScopes()
    
 
  }

  getUsers(){
    this.loading = true
    this.us.getAllUsers().subscribe(
      res => {
        console.log(res)
        res.forEach(e => {
          this.allusers.push(new User(e))
          this.pager.totalOfPages = Math.ceil(this.allusers.length / this.pager.pagesize) 
          this.paginate()
        });
        this.loading = false
      },
      err => {this.loading = false;this.tsr.pop("error","Error","Server Error")}
    )
  }

  getScopes(){
    this.us.getScopes().subscribe(
      res => {
        this.scopes = res
        console.log(res)
      },
      err => this.tsr.pop("error","Error","Server Error")
    )
  }

  saveUser(newUser){
    this.saveLoading = true
    this.us.saveUser(newUser).subscribe(
      res => {
        console.log(res)
        let x = this.users.findIndex(currentUser => currentUser.id == newUser.id)
        if(x >= 0) {
          this.users[x].set(newUser)
          this.allusers[x].set(newUser)
        }
        else
        {
          newUser.id = res.ruId
         this.users.push(newUser)
         this.allusers.push(newUser)
         }
        this.tsr.pop("success","Success","User has been saved")
        this.saveLoading = false
      },
      err =>  {this.tsr.pop("error","Error","Server Error");this.saveLoading = false}

    )
  }

  deleteUser(e){
    this.us.deleteUser(e).subscribe(
      res => {
        let x = this.users.findIndex(el => el.id == e.id)
        console.log(x);
        
        if(x >= 0) {
          this.users.splice(x, 1);
          this.allusers.splice(x,1); 
        }
        this.tsr.pop("success","Success","User has been deleted")
      },
      err =>  { console.log(err);this.tsr.pop("error","Error","Server Error");}

    )
  }
  // deleteUser(user) {
  //   console.log(user);
    
  // }

  edit(e){
    this.child.setUser(e)
    this.child.opencollapse()
  }

  paginate(){
    let tab = this.search()
    let val = (this.pager.selectedpage - 1) * this.pager.pagesize;
    let size = val + this.pager.pagesize
    this.pager.totalOfPages = Math.ceil(tab.length / this.pager.pagesize)
      this.users = tab.slice(val, size) 

      
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
     if( typeof(s) == "string"){
    return s.replace("_"," ").toLocaleLowerCase().trim().includes(p.toLocaleLowerCase().trim())
  }
}
  filtrer(e?){
    this.pager.selectedpage = 1 
    this.paginate()
  }
 
  
  
  search(){
     let res = this.allusers
     for (let key of Object.keys(this.query)) {
      if(this.query[key] != "")
       {
        switch(key) { 
          case "fn": { 
            res =  res.filter(s => this.contains(s.name,this.query[key])) 
             break; 
          } 
          case "ln": { 
            res =  res.filter(s => this.contains( s.lastname,this.query[key])) 
            break; 
         } 
         case "func": { 
          res =  res.filter(s => this.contains(s.function,this.query[key])) 
          break; 
         } 
         case "scope": { 
          res =  res.filter(s => this.contains(s.scope.rsName,this.query[key])) 
          break; 
         } 
         case "scopetype": { 
          res =  res.filter(s => this.contains(s.scope.rsType,this.query[key])) 
          break; 
         } 
         case "role": {           
          res =  res.filter(s => this.contains(s.role,this.query[key])) 
          break; 
         }
         case "email": {           
          res =  res.filter(s => this.contains(s.email,this.query[key])) 
          break; 
         }
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
      case "fn": { 
         if(this.sortkey && this.sortkey == "fn")
           this.ascOrdesc = -this.ascOrdesc
         else {this.ascOrdesc = 1,this.sortkey = "fn"}
         this.allusers = this.allusers.sort((a,b) =>(a.name > b.name ? this.ascOrdesc : -this.ascOrdesc))
         break; 
      } 
      case "ln": { 
        if(this.sortkey && this.sortkey == "ln")
           this.ascOrdesc = -this.ascOrdesc
         else {this.ascOrdesc = 1,this.sortkey = "ln"}
         this.allusers = this.allusers.sort((a,b) =>(a.lastname > b.lastname? this.ascOrdesc : -this.ascOrdesc))         
        break; 
     } 
     case "func": { 
      if(this.sortkey && this.sortkey == "func")
      this.ascOrdesc = -this.ascOrdesc
      else {this.ascOrdesc = 1,this.sortkey = "func"}
    this.allusers = this.allusers.sort((a,b) =>(a.function > b.function? this.ascOrdesc : -this.ascOrdesc))
      break; 
     } 
     case "scope": { 
      if(this.sortkey && this.sortkey == "scope")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "scope"}
    this.allusers = this.allusers.sort((a,b) =>(a.scope.rsName > b.scope.rsName? this.ascOrdesc : -this.ascOrdesc))    
      break; 
     } 
     case "scopetype": { 
      if(this.sortkey && this.sortkey == "scopetype")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "scopetype"}
    this.allusers = this.allusers.sort((a,b) =>(a.scope.rsType > b.scope.rsType? this.ascOrdesc : -this.ascOrdesc))  
      break; 
     } 
     case "role": {    
      if(this.sortkey && this.sortkey == "role")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "role"}
    this.allusers = this.allusers.sort((a,b) =>(a.role > b.role? this.ascOrdesc : -this.ascOrdesc))           
      break; 
     }
     case "email": {    
      if(this.sortkey && this.sortkey == "email")
      this.ascOrdesc = -this.ascOrdesc
    else {this.ascOrdesc = 1,this.sortkey = "email"}
    this.allusers = this.allusers.sort((a,b) =>(a.email > b.email? this.ascOrdesc : -this.ascOrdesc))           
      break; 
     } 
       default: { 
         break; 
      } 
     }
     this.pager.selectedpage = 1
     this.paginate()
  }

  message = {}
  deleteClick(deletePopup, user){
    this.message = {
      title:"Warning",
      msg:"Do you really want to delete the user "+ user.name + " " + user.lastname +" ?",
      icon:"delete",
      confirm : () => {this.deleteUser(user)}
    }
    this.modalService.open(deletePopup)
  }

}
