import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminTabManagerComponent } from './components/admintabmanager/admintabmanager.component';

const routes: Routes = [
    {path:'',component:AdminTabManagerComponent},
    { path:'**',redirectTo:"notFound"}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
