import { Component, OnInit } from '@angular/core';
import { UsersService } from './services/users.service';
import { Router } from '../../node_modules/@angular/router';
import { NgxPermissionsService } from '../../node_modules/ngx-permissions';
import { ToasterConfig } from 'angular5-toaster/dist/angular5-toaster';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'EA';
  user = null

  constructor(private us: UsersService, private router: Router,private permissionsService: NgxPermissionsService){}

  ngOnInit()
  {
    let  perm = [];
 
    //sessionStorage.clear()
    if ( !this.us.getStoredUser() )
    {
      this.router.navigate(['login'])
    } else {
      this.us.user.subscribe(user=>
        {
          //console.log("invoeked ----")
          if(user)
          {
            perm.push(user.ruRole.toUpperCase().trim())
            this.permissionsService.loadPermissions(perm)
          }
        
        }
      )
    }
    
  }

  public toasterconfig : ToasterConfig = 
    new ToasterConfig({tapToDismiss: false,mouseoverTimerStop: true});
}
