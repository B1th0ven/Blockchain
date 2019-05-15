import {
  NgModule
} from '@angular/core';
import {
  CommonModule
} from '@angular/common';

import {
  TableLibraryRoutingModule
} from './table-library-routing.module';
import {
  TablesService
} from './services/tables.service';
import {
  TablesListComponent
} from './components/tables-list/tables-list.component';
import {
  TableDefinitionComponent
} from './components/table-definition/table-definition.component';
import {
  TabsManagerComponent
} from './components/tabs-manager/tabs-manager.component';
import {
  AngularFontAwesomeModule
} from 'angular-font-awesome';
import {
  SharedModule
} from '../shared/shared.module';
import {
  BrowserModule
} from '@angular/platform-browser';
import {
  FormsModule
} from '@angular/forms';
import {
  NgMultiSelectDropDownModule
} from 'ng-multiselect-dropdown';
import {
  MultiselectDropdownModule
} from 'angular-2-dropdown-multiselect';
import {
  AngularMultiSelectModule
} from 'angular2-multiselect-dropdown/multiselect.component';
import {
  NgSelectModule
} from '@ng-select/ng-select';
import {
  NgbModule
} from '@ng-bootstrap/ng-bootstrap';
import {
  DatepickerComponent
} from '../../shared/components/datepicker/datepicker.component';
import {
  PaginationComponent
} from '../../shared/components/pagination/pagination.component';
import {
  DetailsHeaderComponent
} from '../../shared/components/details-header/details-header.component';
import {
  DateFormatPipe
} from '../../shared/pipes/date-format.pipe';
import {
  FileService
} from './services/file.service';
import {
  TableFileUploadGenericComponent
} from './components/table-file-upload-generic/file-upload-generic.component';
import {
  DropdownComponent
} from './components/dropdown/dropdown.component';

@NgModule({
  imports: [
    NgbModule.forRoot(),
    FormsModule,
    CommonModule,
    NgSelectModule,
    AngularMultiSelectModule,
    MultiselectDropdownModule,
    NgMultiSelectDropDownModule.forRoot(),
    TableLibraryRoutingModule,
    AngularFontAwesomeModule,
    SharedModule,
  ],
  declarations: [
    TableFileUploadGenericComponent, DropdownComponent, TablesListComponent, TableDefinitionComponent, TabsManagerComponent, DatepickerComponent, DetailsHeaderComponent
  ],
  providers: [
    DateFormatPipe,
    TablesService,
    FileService
  ]
})
export class TableLibraryModule {}
