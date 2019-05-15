import {
  NgModule
} from '@angular/core';
import {
  CommonModule
} from '@angular/common';
import {
  NgbDateParserFormatter,
  NgbDateAdapter,
  NgbModule
} from '@ng-bootstrap/ng-bootstrap';
import {
  NgbDateCustomParserFormatter
} from '../../shared/formatter/date-formatteur';
import {
  NgbDateNativeAdapter
} from '../../shared/adapters/ngBootstrapApapter';
import {
  BrowserXhr
} from '@angular/http';
import {
  NgProgressBrowserXhr,
  NgProgressModule
} from 'ngx-progressbar';
import {
  ClickOutsideModule
} from 'ng-click-outside';
import {
  SelectDropDownModule
} from 'ngx-select-dropdown';
import {
  MultiselectDropdownModule
} from 'angular-2-dropdown-multiselect';
import {
  ReactiveFormsModule, FormsModule
} from '@angular/forms';
import {
  TagInputModule
} from 'ngx-chips';
import {
  NgSelectModule
} from '@ng-select/ng-select';
import {
  HttpClientModule
} from '@angular/common/http';
import {
  AngularMultiSelectModule
} from 'angular2-multiselect-dropdown/multiselect.component';
import {
  ToasterModule
} from 'angular5-toaster/dist/angular5-toaster';
import {
  AngularFontAwesomeModule
} from 'angular-font-awesome';
import {
  TimespanPipe
} from '../../shared/pipes/timespan.pipe';
import {
  BignumberPipe
} from '../../shared/pipes/bignumber.pipe';
import {
  DatasetFilesComponent
} from '../../components/dataset-files/dataset-files.component';
import {
  FilterPipe
} from '../../shared/pipes/filter.pipe';
import {
  FilesFilterPipe
} from '../../shared/pipes/files-filter.pipe';
import {
  SortPipe
} from '../../shared/pipes/sort.pipe';
import {
  UndersoreToSpacePipe
} from '../../shared/pipes/undersore-to-space.pipe';
import {
  LoaderComponent
} from '../../shared/loaders/loader/loader.component';
import {
  AutoSlashDirective
} from '../../shared/directives/auto-slash.directive';
import {
  StringLimitPipe
} from '../../shared/pipes/string-limit.pipe';
import {
  DateFormatPipe
} from '../../shared/pipes/date-format.pipe';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { CheckboxSliderComponent } from '../../shared/components/checkbox-slider/checkbox-slider.component';
import { NgxPermissionsModule } from 'ngx-permissions';


@NgModule({
  imports: [
    CommonModule,
    AngularFontAwesomeModule,
    FormsModule
  ],
  declarations: [
    TimespanPipe,
    BignumberPipe,
    FilterPipe,
    FilesFilterPipe,
    UndersoreToSpacePipe,
    LoaderComponent,
    AutoSlashDirective,
    StringLimitPipe,
    DateFormatPipe,
    SortPipe,
    PaginationComponent,
    CheckboxSliderComponent,

  ],
  providers: [

  ],
  exports: [
    TimespanPipe,
    BignumberPipe,
    FilterPipe,
    FilesFilterPipe,
    UndersoreToSpacePipe,
    LoaderComponent,
    AutoSlashDirective,
    StringLimitPipe,
    DateFormatPipe,
    SortPipe,
    PaginationComponent,
    CheckboxSliderComponent,
    NgxPermissionsModule,
  ]
})
export class SharedModule {}
