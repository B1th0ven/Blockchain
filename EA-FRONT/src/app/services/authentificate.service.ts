import { Injectable } from '@angular/core';
import { NgxPermissionsService } from '../../../node_modules/ngx-permissions';

@Injectable()
export class AuthentificateService {

  user= {
    username:"user9000",
  }

  constructor(private permissionsService: NgxPermissionsService) { }

  login(username,password){
    //TODO
    return new Promise(resolve=>resolve(this.user))
  }

  logout(){
    localStorage.removeItem("user")
    localStorage.removeItem("token")
    this.permissionsService.flushPermissions()
  }
}
