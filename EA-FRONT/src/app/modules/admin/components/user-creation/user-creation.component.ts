import { Component, OnInit, Output,EventEmitter, Input} from '@angular/core';
import { User } from '../../../../shared/models/user';

declare let $: any;


@Component({
  selector: 'app-user-creation',
  templateUrl: './user-creation.component.html',
  styleUrls: ['./user-creation.component.scss']
})
export class UserCreationComponent implements OnInit {
  @Input() scopes:any
  @Input() saveLoading:boolean
  @Output() usersaved: EventEmitter< any > = new EventEmitter < any >()
  @Output() userdeleted: EventEmitter< any > = new EventEmitter < any >()
  isOpened:boolean = false

  user:User = new User();

  constructor() { }

  ngOnInit() {

  }

  init(){
    this.user = new User()
    this.isOpened = !this.isOpened
  }

  saveUser(){
    this.user.scope = this.scopes.find(e => e.rsId == this.user.scope)
    console.log("Emitted",this.user);
    this.usersaved.emit(this.user);
    this.user = new User()
    this.isOpened = false
  }

  setUser(u:User){
    let copie = Object.assign({}, u);
    this.user.jsonToUser(copie);
    this.user.scope = this.user.scope.rsId
  }

  deleteUser(){
    this.userdeleted.emit(this.user)
    this.user = new User()
    this.isOpened = false
  }

  opencollapse(){
    $('#collapseExample').collapse('show');
    this.isOpened = true
  }
  isEmailValid(){
    return this.user.email.includes("@") && this.user.email.includes(".")
  }
  isUserValid(){
    return this.user.lastname  &&
           this.user.name  && this.user.login  && this.user.role  && this.user.scope  && this.user.function }

}
