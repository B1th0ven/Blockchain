import { Component, OnInit } from '@angular/core';
import { UsersService } from '../services/users.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.scss']
})
export class BaseComponent implements OnInit {

  constructor(private us:UsersService,private router : Router) { }

  ngOnInit() {
    this.us.user.subscribe(user=>{
      if ( !user )
      {
        //REMOVED FOR UAT
        //this.router.navigate(['login'])
      }

    })
  }

}
