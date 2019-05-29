import {
  Component,
  OnInit
} from '@angular/core';
import {
  UsersService
} from '../services/users.service';
import { Router } from '@angular/router';
import { NgxPermissionsService } from '../../../node_modules/ngx-permissions';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loadingInit:boolean = false

  constructor(private us: UsersService, private router: Router,private permissionsService: NgxPermissionsService) {}

  ngOnInit() {
    if ( this.us.getStoredUser() )
    {
      console.log("USERRRRRRRRR", this.us.getStoredUser())
      let perm = []
      perm.push(this.us.getStoredUser().ruRole.toUpperCase().trim())
      this.permissionsService.flushPermissions()
      this.permissionsService.loadPermissions(perm)
      this.router.navigate(['study'])
    }

    // this.loadingInit = true
    // this.us.check().subscribe(
    //   res => { 
    //     if(res != null && res != "") {
    //     this.us.storeUser(res)
    //     let perm = []
    //     perm.push(this.us.getStoredUser().ruRole.toUpperCase().trim())
    //    this.permissionsService.flushPermissions()
    //    this.permissionsService.loadPermissions(perm)
    //     this.router.navigate(['study'])
    //     } else this.router.navigate(['unauthorized'])
    //     this.loadingInit = false
    //   },
    //   err => {this.router.navigate(['unauthorized']);this.loadingInit = false}
    // )
  }

  username: string
  loading: boolean = false;
  error: string = null

  onLogInClick() {
    this.error = null
    this.loading = true;
    this.us.login(this.username).subscribe(user => {
        console.log("USER INFO", user);
        this.loading = false;
        this.us.storeUser(user)
        let perm = []
        perm.push(user.ruRole.toUpperCase().trim())
        this.permissionsService.flushPermissions()
        this.permissionsService.loadPermissions(perm)
        this.router.navigate(['study'])
      },
      err => {
        this.error = err;
        this.loading = false;
      })
  }
}
