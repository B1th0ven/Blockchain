import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { TabsManagerComponent } from './components/tabs-manager/tabs-manager.component';

const routes: Routes = [
    {path:'',component:TabsManagerComponent},
    { path:'**',redirectTo:"notFound"}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TableLibraryRoutingModule { }
