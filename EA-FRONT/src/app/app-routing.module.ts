import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { StudyListComponent } from './components/study-list/study-list.component';
import { LoginComponent } from './login/login.component';
import { BaseComponent } from './base/base.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';


const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path:'unauthorized',
    component: UnauthorizedComponent
  },
  { path: '',
    component:BaseComponent,
    children:[
      { path: 'study',
        component: StudyListComponent
      },
      {
        path: 'tables',
        loadChildren: 'app/modules/table-library/table-library.module#TableLibraryModule'
      },
      {
        path: 'admin',
        loadChildren: 'app/modules/admin/admin.module#AdminModule'
      },
      {
        path: 'home',
        component: HomeComponent
      },
      {
        path:'',
        redirectTo:'/study',
        pathMatch:'full'
      }
    ]
  },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
