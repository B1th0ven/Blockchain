import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminTabManagerComponent } from './components/admintabmanager/admintabmanager.component';
import { SharedModule } from '../shared/shared.module';
import { AdminRoutingModule } from './admin-routing.module';
import { FormsModule } from '../../../../node_modules/@angular/forms';
import { NgbModule } from '../../../../node_modules/@ng-bootstrap/ng-bootstrap';
import { AngularFontAwesomeModule } from '../../../../node_modules/angular-font-awesome';
import { UsersTableComponent } from './components/users-table/users-table.component';
import { UserCreationComponent } from './components/user-creation/user-creation.component';

@NgModule({
  imports: [
    NgbModule.forRoot(),
    FormsModule,
    CommonModule,
    SharedModule,
    AdminRoutingModule,
    AngularFontAwesomeModule,
  ],
  declarations: [AdminTabManagerComponent, UsersTableComponent, UserCreationComponent]
})
export class AdminModule { }
