import { Component, OnInit } from '@angular/core';
import { UsersService } from '../../services/users.service';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {

  constructor(private us:UsersService) { }

  user = null

  ngOnInit() {
    this.us.user.subscribe(user=>
      {
        this.user = user
      }
    )
  }

  logout()
  {
    this.us.logout()
  }

}
