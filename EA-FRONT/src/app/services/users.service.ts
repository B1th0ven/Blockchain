import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { environment } from '../../environments/environment';
import { Observable, Subject, BehaviorSubject } from 'rxjs';
import { Router } from '../../../node_modules/@angular/router';
import { NgxPermissionsService } from '../../../node_modules/ngx-permissions';
import { User } from '../shared/models/user';

@Injectable()
export class UsersService {

  readonly USER_STORAGE_NAME = "USER"

  user:BehaviorSubject<any> = new BehaviorSubject(null)

  constructor(private http:Http,private router:Router,private permissionsService: NgxPermissionsService) {
    let user = this.getStoredUser()
    if ( user ) this.user.next(user)
  }

  login(username)
  {
    return this.http.post(environment.api_endpint+"users/login",username).map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  check()
  {
    return this.http.get(environment.api_endpint+"users/kerb").map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  storeUser(user:any)
  {
    this.user.next(user)
    localStorage.setItem(this.USER_STORAGE_NAME,JSON.stringify(user))
  }

  getStoredUser()
  {
    let user = localStorage.getItem(this.USER_STORAGE_NAME)
    return (user)? JSON.parse(user):null
  }

  logout()
  {
    this.user.next(null)
    localStorage.removeItem(this.USER_STORAGE_NAME)
    this.router.navigate(['login'])
    this.permissionsService.flushPermissions()
  }

  getAllUsers(){
    return this.http.get(environment.api_endpint+"users").map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  getRoleByStID(st_id,usr_id){
    // if(usr_id == 'u006993')
    // return 'PRODUCER'
    // else if(usr_id == 'u006992')
    // return 'PRODUCER'
    // else
    // return 'PRODUCER'
    return this.http.get(environment.api_endpint+"users/roles/"+st_id+"/"+usr_id).map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  getRoleByExpID(tab_id,usr_id){
    //  if(usr_id == 'u006993')
    // return 'PRODUCER'
    // else if(usr_id == 'u006992')
    // return 'PRODUCER'
    // else
    // return 'PRODUCER'
    return this.http.get(environment.api_endpint+"users/roles/table/"+tab_id+"/"+usr_id).map(res=>res.json()).catch(err=> Observable.throw(err))

  }

  getStudyRolesException(st_id){
   // return this.http.get(environment.api_endpint+"users/roles/exceptions/"+st_id).map(res=>res.json()).catch(err=> Observable.throw(err))
    return this.http.get(environment.api_endpint+"users/roles/"+st_id).map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  addOrUpdateException(st_id,usr_id,role){
    let body = {st_id:String(st_id),usr_id:String(usr_id),role:String(role)}
    return this.http.post(environment.api_endpint+"users/roles/exceptions",body).map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  getScopes(){
    return this.http.get(environment.api_endpint+"scopes").map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  saveUser(user:User){
    return this.http.post(environment.api_endpint+"users/save",user.mapToApi()).map(res=>res.json()).catch(err=> Observable.throw(err))
  }

  deleteUser(user:User){
    return this.http.delete(environment.api_endpint+"users/"+user.id).map(res=>res.json()).catch(err=> Observable.throw(err))
  }
  
  
}
