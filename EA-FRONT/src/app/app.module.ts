import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';

import { HttpModule, BrowserXhr } from '@angular/http';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule } from '@angular/forms';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { ProgressCircleModule } from './custom-modules/progress-circle.module';
import { BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { ClickOutsideModule } from 'ng-click-outside';
import { SelectDropDownModule } from 'ngx-select-dropdown';
import { MultiselectDropdownModule } from 'angular-2-dropdown-multiselect';
import { TagInputModule } from 'ngx-chips';
import { ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { NgbModule, NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';
import {ToasterModule} from 'angular5-toaster/dist';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { NgProgressModule, NgProgressBrowserXhr } from 'ngx-progressbar';

import { AppComponent } from './app.component';
import { DatasetCreateComponent } from './components/dataset-create/dataset-create.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { HomeComponent } from './components/home/home.component';
import { FileService } from './services/file.service';
import { AuthentificateService } from './services/authentificate.service';
import { ControlService } from './services/control.service';
import { DatasetService } from './services/dataset.service';
import { FilebrowserComponent } from './components/filebrowser/filebrowser.component';
import { DatasetListComponent } from './components/dataset-list/dataset-list.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { SearchPipePipe } from './shared/pipes/search-pipe.pipe';
import { FileuploadComponent } from './components/fileupload/fileupload.component';
import { DatasetControlComponent } from './components/dataset-control/dataset-control.component';
import { DatasetFilesComponent } from './components/dataset-files/dataset-files.component';
import { WebsocketService } from './services/websocket.service';
import { StudyListComponent } from './components/study-list/study-list.component';
import { StudyCreateComponent } from './components/study-create/study-create.component';
import { StudyListSearchComponent } from './components/study-list-search/study-list-search.component';
import { StudyDefinitionComponent } from './components/study-definition/study-definition.component';
import { DatasetManagementComponent } from './components/dataset-management/dataset-management.component';
import { RunManagementComponent } from './components/run-management/run-management.component';
import { StudyValidationComponent } from './components/study-validation/study-validation.component';
import { StudyAccessRightComponent } from './components/study-access-right/study-access-right.component';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { StudyDatasetCreateComponent } from './components/study-dataset-create/study-dataset-create.component';
import { StudyService } from './services/study.service';
import { NgbDateCustomParserFormatter } from './shared/formatter/date-formatteur';
import { StudyDatasetComponent } from './components/study-dataset/study-dataset.component';
import { HttpClientModule } from '@angular/common/http';
import { RunGeneralComponent } from './components/run-management/run-general/run-general.component';
import { RunDecrementComponent } from './components/run-management/run-decrement/run-decrement.component';
import { RunDecrementTablesComponent } from './components/run-management/run-decrement/run-decrement-tables/run-decrement-tables.component';
import { RunDecrementDefinitionComponent } from './components/run-management/run-decrement/run-decrement-definition/run-decrement-definition.component';
import { FormValidatorService } from './services/form-validator.service';
import { NgbDateNativeAdapter } from './shared/adapters/ngBootstrapApapter';
import { IbnrManualComponent } from './components/run-management/ibnr-manual/ibnr-manual.component';
import { IbnrAmountComponent } from './components/run-management/ibnr-amount/ibnr-amount.component';
import { FileUploadGenericComponent } from './components/file-upload-generic/file-upload-generic.component';
import { IbnrControlService } from './services/ibnr-control.service';
import { RunService } from './services/run.service';
import { HoverOverComponent } from './components/hover-over/hover-over.component';
import { HoverOverService } from './components/hover-over/hover-over.service';
import { SharedModule } from './modules/shared/shared.module';
import { MultiselectComponent } from './shared/components/multiselect/multiselect.component';
import { FooterComponent } from './components/footer/footer.component';
import { RunInformationComponent } from './components/run-management/run-information/run-information.component';
import { TimstampFormatPipe } from './shared/pipes/timstamp-format.pipe';
import { DropdownComponent } from './shared/components/dropdown/dropdown.component';
import { StudyValidationSectionComponent } from './components/study-validation/study-validation-section/study-validation-section.component';
import { LoginComponent } from './login/login.component';
import { BaseComponent } from './base/base.component';
import { UsersService } from './services/users.service';
import { UserInfoComponent } from './components/user-info/user-info.component';
import { DecrementsFilterPipe } from './shared/pipes/decrements-filter.pipe';
import { RunDecTabDefComponent } from './components/run-management/run-decrement/run-dec-tab-def/run-dec-tab-def.component';
import { StoreService } from './services/store/store.service';
import { ExpectedCalibrationModalComponent } from './components/run-management/run-decrement/expected-calibration-modal/expected-calibration-modal.component';
import { NgxPermissionsModule } from 'ngx-permissions';
import { DatasetDataPrivacyComponent } from './components/dataset-data-privacy/dataset-data-privacy.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { ConfigService } from './services/config.service';
import { DatasetControlErrorComponent } from './components/dataset-control-error/dataset-control-error.component';
import { DatasetControlSingleComponent } from './components/dataset-control-single/dataset-control-single.component';
import { DatasetControlManagementComponent } from './components/dataset-control-management/dataset-control-management.component';
import { DatasetControlDataService } from './services/dataset-control-data.service';
import { DatasetControlLogicService } from './services/dataset-control-logic.service';
import { DatasetDesignService } from './services/dataset-design.service';


@NgModule({
  declarations: [
    AppComponent,
    DatasetCreateComponent,
    PageNotFoundComponent,
    HomeComponent,
    FilebrowserComponent,
    DatasetListComponent,
    SidebarComponent,
    NavbarComponent,
    SearchPipePipe,
    FileuploadComponent,
    DatasetControlComponent,
    StudyListComponent,
    StudyCreateComponent,
    StudyListSearchComponent,
    StudyDefinitionComponent,
    DatasetManagementComponent,
    RunManagementComponent,
    StudyValidationComponent,
    StudyAccessRightComponent,
    StudyDatasetCreateComponent,
    StudyDatasetComponent,
    RunGeneralComponent,
    RunDecrementComponent,
    RunDecrementTablesComponent,
    RunDecrementDefinitionComponent,
    DatasetFilesComponent,
    IbnrManualComponent,
    IbnrAmountComponent,
    FileUploadGenericComponent,
    HoverOverComponent,
    FooterComponent,
    RunInformationComponent,
    TimstampFormatPipe,
    DropdownComponent,
    StudyValidationSectionComponent,
    LoginComponent,
    BaseComponent,
    UserInfoComponent,
    DecrementsFilterPipe,
    RunDecTabDefComponent,
    ExpectedCalibrationModalComponent,
    DatasetDataPrivacyComponent,
    UnauthorizedComponent,
    MultiselectComponent,
    DatasetControlErrorComponent,
    DatasetControlSingleComponent,
    DatasetControlManagementComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    AngularFontAwesomeModule,
    HttpModule,
    ProgressCircleModule,
    BrowserAnimationsModule,
    NgbModule.forRoot(),
    ReactiveFormsModule,
    TagInputModule,
    NgSelectModule,
    AngularMultiSelectModule,
    MultiselectDropdownModule,
    NgMultiSelectDropDownModule.forRoot(),
    HttpClientModule,
    NgbModule,
    NgProgressModule,
    ClickOutsideModule,
    SelectDropDownModule,
    ToasterModule,
    SharedModule,
    NgxPermissionsModule.forRoot(),
  ],
  providers: [
    AuthentificateService,
    DatasetService,
    DatasetControlDataService,
    DatasetControlLogicService,
    DatasetDesignService,
    FileService,
    ControlService,
    IbnrControlService,
    WebsocketService,
    StudyService,
    FormValidatorService,
    RunService,
    HoverOverService,
    UsersService,
    StoreService,
    ConfigService,
    {provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter},
    {provide: NgbDateAdapter, useClass: NgbDateNativeAdapter},
    {provide: BrowserXhr, useClass: NgProgressBrowserXhr}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
