import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  ChangeDetectorRef,
  HostListener
} from '@angular/core';
import {
  Study
} from '../../shared/models/study';
import {
  StudyService
} from '../../services/study.service';
import {
  timestamp
} from 'rxjs/operators/timestamp';
import {
  IMultiSelectSettings,
  IMultiSelectOption
} from 'angular-2-dropdown-multiselect';
import {
  IOption
} from 'ng-select';
import {
  HttpResponse,
  HttpEventType
} from '@angular/common/http';
import {
  ToasterService
} from 'angular5-toaster/dist/src/toaster.service';
import { SCREEN, HoverOverService } from '../hover-over/hover-over.service';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { UsersService } from '../../services/users.service';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { date } from '../../shared/models/date';

import { catchError, map, debounceTime, switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-study-definition',
  templateUrl: './study-definition.component.html',
  styleUrls: ['./study-definition.component.scss']
})
export class StudyDefinitionComponent implements OnInit {

  @Input() study: Study

  @Output() save: EventEmitter<any> = new EventEmitter<any>()
  @Output() continue: EventEmitter<any> = new EventEmitter<any>()
  @Output() delete: EventEmitter<any> = new EventEmitter<any>()
  @Output() statusChange: EventEmitter<any> = new EventEmitter<any>()

  err: string
  haveOption: boolean = false
  crurrentClientPage : number ; 
  mytt: string ; 
  treaties: Array<any> = new Array<any>()
  groups: Array<any> = new Array<any>()
  clients: Array<any> = new Array<any>()
  Allclients: Array<any> = new Array<any>()
  business: Array<any> = new Array<any>()
  clienttypes: Array<any> = new Array<any>()
  engines: Array<any> = new Array<any>()
  datasources: Array<any> = new Array<any>()
  countries: Array<any> = new Array<any>()
  clientSearchCriteria : string ; 
  screen = SCREEN.STUDY
  deleteDataCheck: boolean = false;

  typeahead = new EventEmitter<string>();

  constructor(
    private tsr: ToasterService,
    private studyService: StudyService,
    public hs: HoverOverService,
    private modalService: NgbModal,
    private us: UsersService,
    private permissionsService: NgxPermissionsService,
    private cd: ChangeDetectorRef
  ) {
    
    this.crurrentClientPage=0 ; 
    this.typeahead
      .pipe(
        debounceTime(200),
        switchMap(term => this.searchTreaties(term))
      )
      .subscribe(items => {
        this.treaties = items;
        this.cd.markForCheck();
      }, (err) => {
        console.log('error', err);
        this.treaties = [];
        this.cd.markForCheck();
      });
  }

  searchTreaties(term) {
    this.treaties = []
    this.treaties.push({ "rtId": "", "rtName": "Loading..." })
    if (term.length == 0) {
      return []
    }
    return this.studyService.searchTreaties(term)
  }

  ngOnInit() {
      this.clientSearchCriteria = "";
      if ( !this.study.id )
          this.study.creator = this.us.getStoredUser();

      if ( this.study.clientGroup && Number( this.study.clientGroup ) ) {
          this.studyService.getClientsByParentGroup( Number( this.study.clientGroup ) ).subscribe(
              res => {
                  this.clients = res.sort( function( a, b ) {
                      if ( a['rcnShortName'] < b['rcnShortName'] ) return -1;
                      else if ( a['rcnShortName'] > b['rcnShortName'] ) return 1;
                      return 0;
                  } );
                  this.clients.unshift( {
                      "rcnShortName": "Multiple",
                      "rcnId": "multi"
                  } )
                  this.clients.unshift( {
                      "rcnShortName": "Other",
                      "rcnId": "other"
                  } )
                  // this.clients.unshift({
                  //   "rcnShortName": "",
                  //   "rcnId": ""
                  // })
              } )
      }

      if ( this.study.client && Number( this.study.client ) ) {
          this.studyService.getTreatieByClient( Number( this.study.client ) ).subscribe(
              res => {
                  this.treaties = res
              } )

          if ( !this.study.clientGroup || this.study.clientGroup == "" || this.study.clientGroup == "idp" ) {
              this.study.clientGroup = "idp"
              this.studyService.getClienById( Number( this.study.client ) ).subscribe( res => this.clients = [res],
                  err => this.err = "Could not fetch client" )
          }
      }
      // else {
      //   this.studyService.getAllClients().subscribe(
      //     res => {
      //       this.clients = res
      //        this.clients.sort(function (a, b) {
      //         if (a['rcnShortName'] < b['rcnShortName']) return -1;
      //         else if (a['rcnShortName'] > b['rcnShortName']) return 1;
      //         return 0;
      //       });
      //       this.Allclients = this.clients
      //     }
      //   )
      // }

      this.studyService.getParams()
          .subscribe( data => {
              this.clienttypes = data[0]
              this.engines = data[1]
              this.groups = data[2]
              this.groups.push( { "rpgName": "Independent", "rpgId": "idp" } )



              if ( this.groups && this.groups[0].rpgId != "" ) {
                  this.groups.sort( function( a, b ) {
                      if ( a['rpgName'] < b['rpgName'] ) return -1;
                      else if ( a['rpgName'] > b['rpgName'] ) return 1;
                      return 0;
                  } );
                  this.groups.unshift( {
                      "rpgName": "Multiple",
                      "rpgId": "multi"
                  } )
                  this.groups.unshift( {
                      "rpgName": "Other",
                      "rpgId": "other"
                  } )
                  this.groups.unshift( {
                      "rpgName": ".",
                      "rpgId": ""
                  } )
              }

              //this.clients      = data[3]
              //this.Allclients = data[3]
              //this.treaties     = data[4]
              this.business = data[3]
              this.datasources = data[4]
              this.countries = data[5]
              this.countries.sort( function( a, b ) {
                  if ( a['rcName'] < b['rcName'] ) return -1;
                  else if ( a['rcName'] > b['rcName'] ) return 1;
                  return 0;
              } );

              this.haveOption = true

              this.Allclients = data[6].content;
              if ( this.Allclients && this.Allclients[0].rcnId != "other" ) {
                  this.Allclients.unshift( {
                      "rcnShortName": "Multiple",
                      "rcnId": "multi"
                  } )
                  this.Allclients.unshift( {
                      "rcnShortName": "Other",
                      "rcnId": "other"
                  } )
                  // this.Allclients.unshift({
                  //   "rcnShortName": "",
                  //   "rcnId": ""
                  // })

              }
              if ( !this.study.clientGroup || this.clients.length==0 ) {
                  this.clients = this.Allclients
              }

          }, err => this.err = "Could not fetch Params" );

  }

  



  saveClick() {
    if (this.valid()) {
      this.loading = true
      this.studyService.postStudy(this.study).subscribe(
        data => {
          this.save.emit(this.study);
          this.err = "";
          this.loading = false
          this.tsr.pop("success", "Saved", "Study saved")
        },
        err => {
          this.tsr.pop("error", "Error", "Study not saved")
          this.err = "An error occured with the server.";
          this.loading = false;

        }
      )

      //this.study.save()
    }
  }

  concatinate(a, b) {
    if (a && b)
      return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }

  getPermissions(res) {
    this.permissionsService.flushPermissions()
    this.permissionsService.addPermission(this.us.getStoredUser().ruRole)
    this.permissionsService.addPermission(res.primaryRole)
    res.secondaryRoles.forEach(element => {
      this.permissionsService.addPermission(element)
    });

  }

  continueClick() {
    if (this.valid()) {
      /*
      if (this.study.id && this.study.code ){
        this.continue.emit(this.study);
        return
      }
      */




      let checkIfStudyiSaved = this.study.id
      this.loading = true
      if (this.permissionsService.getPermission("STUDY_WRITER") || !this.study.id) {
        this.studyService.postStudy(this.study).subscribe(
          data => {

            this.study.id = data["stId"];
            this.study.code = data["stCode"];
            this.study.status = data["stStatus"];
            this.err = ""
            if (!checkIfStudyiSaved) {
              this.us.getRoleByStID(this.study.id, this.us.getStoredUser().ruId).subscribe(
                res => {
                  console.log(res); this.getPermissions(res);
                  this.loading = false
                  this.continue.emit(this.study); //Redirect user to datasets
                },

                err => this.tsr.pop('error', "Error", "Server Error")
              )
            } else {
              this.loading = false
              this.continue.emit(this.study);
            }


            this.tsr.pop("success", "Saved", "Study saved")
          },
          err => {

            this.tsr.pop("error", "Error", "Study not saved")

            this.err = "An error occured with the server.";
            this.loading = false;

          }
        )
      } else {
        this.loading = false
        this.continue.emit(this.study);
      }

      //this.study.save()
    }
  }



  valid() {
    return this.study.valid() && !this.isLoading()
  }

  uploadFile(e, study, loadfile) {
    let filechanged = study.attachedFilePath && study.attachedFilePath.trim() != ""

    if (filechanged) {
      this.message = {
        title: "Warning",
        msg: "Do you really want to override file?",
        icon: "warning",
        confirm: () => { this.loadFile(e, study) }
      }
      this.modalService.open(loadfile)
    }
    else this.loadFile(e, study)
  }

  chargedFile = null
  percentDone = -1
  loadFile(e, study) {
    let myFile = e.target.files[0];
    this.chargedFile = null;
    let fileType;
    let fileSize;
    let extensioncheck = true;
    let allowedExtensions = /(\.docx|\.doc|\.pdf|\.txt|\.pptx|\.ppt|\.xls|\.xlsx|\.rar|\.zip)$/i;

    if (!allowedExtensions.exec(myFile.name)) {
      extensioncheck = false;
      this.tsr.pop("warning", "Warning", "The file has not been uploaded, study documentation file supported formats are pdf, doc/docx, ppt/pptx, xls/xlsx, txt, rar, zip.")
    } else {
      fileType = myFile.name.split('.').pop();
      let _formData = new FormData();
      _formData.append("MyFile", myFile);
      let body = _formData;
      let filechanged = study.attachedFilePath && study.attachedFilePath.trim() != ""
      study.attachedFilePath = myFile.name;
      this.percentDone = 0;
      this.studyService.uploadAttachedFile(body)
        .subscribe(
          event => {

            // Via this API, you get access to the raw event stream.
            // Look for upload progress events.
            if (event.type === HttpEventType.UploadProgress) {
              fileSize = event.total;
              // This is an upload progress event. Compute and show the % done:
              this.percentDone = Math.round(100 * event.loaded / event.total);

              if (this.percentDone == 100) {
                if (filechanged)
                  this.tsr.pop("info", "Override", "The file has been uploaded and has replaced the previous one.")
                else
                  this.tsr.pop("success", "Upload", "File uploaded successfully.")
              }


            } else if (event instanceof HttpResponse) {
              let file = event.body;

              if (file["path"] && file["path"] != "")
                study.attachedFilePath = file["path"]
              else
                this.percentDone = -2

            }
          });
    }
  }

  download() {
    this.studyService.openFile(this.fileName(this.study.attachedFilePath))
  }

  fileName(attachedFilePath) {
    if (attachedFilePath) return String(attachedFilePath).trim().split("/").pop()
    else return ""
  }

  // clientsOfParent(clients: Array < any > , clientsOfParent) {
  //   /*if (!clients || !clientsOfParent)
  //     return []

  //   return clients.filter(
  //     c => c.rcnRpgId == clientsOfParent
  //   )*/
  //   /*if(clientsOfParent && clientsOfParent == ""){
  //     console.log(clientsOfParent);

  //     console.log(this.Allclients);

  //     return this.Allclients
  //   }*/
  //   // console.log(clients);

  //   // if(clients && clients.length>3) {
  //   //   if(clients[2].rcnId != "multi") {
  //   //     clients.unshift({
  //   //       "rcnShortName": "Multiple",
  //   //       "rcnId": "multi"
  //   //     })
  //   //   }
  //   //   if(clients[1].rcnId != "other") {
  //   //       clients.unshift({
  //   //       "rcnShortName": "Other",
  //   //       "rcnId": "other"
  //   //     })
  //   //   }
  //   //   if(clients[0].rcnId != "") {
  //   //     clients.unshift({
  //   //       "rcnShortName": "",
  //   //       "rcnId": ""
  //   //     })
  //   //   }
  //   // }
  //   return clients ;
  // }

  TreatyOfclient(treaties: Array<any>, client) {
    if (!treaties)
      return []

    if (!client || client == "")
      return treaties

    return treaties.filter(
      t => t.rtRcnId == client
    )
  }

  disableTreaty() {
    if (!this.study.client || this.study.client == "") return true
    else if (this.study.client == "other") return true
    else if (this.study.client == "multi") return true
    return false
  }

  treatyState = {
    loading: false,
    loaded: false,
    error: false
  }
  onClientChanged(treatySelected?: string) {
    this.study.treatyNumber = treatySelected ? treatySelected : "";

    if (isNaN(Number(this.study.client))) {
      if (this.study.client == "multi") {
        this.study.treatyNumber = "multi"
        this.study.clientCountry = ""
        this.study.clientGroup = "multi"
      } else {
        if (this.study.client == "other") {
          this.study.clientGroup = "other"
        }
        this.study.treatyNumber = treatySelected ? treatySelected : "";

        let client = this.clients.find(c => c.rcnId == this.study.client)
        //
        if (client)
          this.study.clientCountry = (client['refCountryById']) ? client['refCountryById']["rcId"] : "";
      }
      return
    }

    if (this.study.client && this.study.client != "") {

      let client = this.clients.find(c => c.rcnId == this.study.client);
      let group = this.groups.find(g => g.rpgId == client.rcnRpgId)
      // console.log("herherherherherh checking",group)
      if (group)
        this.study.clientGroup = client.rcnRpgId
      else
        this.study.clientGroup = "idp"
    }

    // console.log("------------> client,groupe,country",this.study.client,this.study.clientGroup,this.study.clientCountry)
    // console.log(this.groups.find(g => g.rpgId == this.study.clientGroup))



    this.treatyState.error = false;
    this.treatyState.loaded = false;
    this.treatyState.loading = true;

    this.studyService.getTreatieByClient(Number(this.study.client)).subscribe(
      res => {
        this.treaties = res;
        this.treatyState.error = false;
        this.treatyState.loaded = true;
        this.treatyState.loading = false;
        if (this.study.client == "multi") {
          this.study.treatyNumber = "multi"
          this.study.clientCountry = ""
        } else {
          this.study.treatyNumber = treatySelected ? treatySelected : "";

          let client = this.clients.find(c => c.rcnId == this.study.client)
          //
          if (client)
            this.study.clientCountry = (client['refCountryById']) ? client['refCountryById']["rcId"] : "";
        }
      }, err => {
        this.treaties = [];
        this.treatyState.error = true;
        this.treatyState.loaded = true;
        this.treatyState.loading = false;
      }
    )
  }


  getPageOfClients() {

    if (( !this.study.clientGroup || this.study.clientGroup=="" || this.study.clientGroup=="other" || 
    this.study.clientGroup=="multi" || this.study.clientGroup=="idp") && (!this.clientSearchCriteria || this.clientSearchCriteria.length==0  )) {
    this.studyService.getPageOfClients(this.crurrentClientPage+1, 30).subscribe(res => {this.clients = this.clients.concat(res.content);
      this.crurrentClientPage=res.number} ,
    err => "Could not fetch Clients"  );
    }
}

typingTimeout = null;
  filter = () => {
    clearTimeout(this.typingTimeout)
    this.typingTimeout = setTimeout(() => {
      this.studyService.getPageOfClients(0, 30, this.clientSearchCriteria).subscribe(res => {this.clients = res.content;
        this.crurrentClientPage=0 } ,err => "Could not fetch Clients",  ()=> 
        {
          if ("multi".includes(this.clientSearchCriteria) ) 
              this.clients.unshift({"rcnShortName": "Multiple","rcnId": "multi"}) ;
          if ("other".includes(this.clientSearchCriteria) ) 
              this.clients.unshift({"rcnShortName": "Other",  "rcnId": "other" })
        })
    }, 200);
  }

clientSearch(event) {
  if ( !this.study.clientGroup || this.study.clientGroup=="" || this.study.clientGroup=="other" || 
  this.study.clientGroup=="multi" || this.study.clientGroup=="idp"  ){
  this.clientSearchCriteria=event.path[0].previousSibling.nextElementSibling.attributes[0].ownerElement.value ; 

  if (this.clientSearchCriteria && this.clientSearchCriteria.length>0) {
    this.filter()
  } else {
    this.clients=this.clients=this.Allclients ;  
    }
  }
  }

  closeClient(t){
    this.clientSearchCriteria="" ;
    console.log(this.study.client)
    if (!this.study.client || isNaN(Number(this.study.client)) ) {
      if (!this.study.clientGroup || this.study.clientGroup=="" || this.study.clientGroup=="other" || 
        this.study.clientGroup=="multi" || this.study.clientGroup=="idp")
            this.clients=this.Allclients ; 
    }
  }






  myOptions: Array<IOption> = [{
    label: 'Belgium',
    value: 'BE'
  },
  {
    label: 'Luxembourg',
    value: 'LU'
  },
  {
    label: 'Netherlands',
    value: 'NL'
  }
  ];

  clientState = {
    loading: false,
    loaded: false,
    error: false
  }

  onGroupChanged() {
    this.study.client = ""
    this.crurrentClientPage=0
    if (this.study.clientGroup == "") {
      console.log(this.Allclients);

      this.clients = this.Allclients
      return
    }
    if (isNaN(Number(this.study.clientGroup))) {
      this.clients = this.Allclients
      if (this.study.clientGroup == "multi" || this.study.clientGroup == "other")
        this.study.client = this.study.clientGroup
      else
        this.study.client = ""

          ;
      this.onClientChanged()
      return
    }

    this.clientState.error = false;
    this.clientState.loaded = false;
    this.clientState.loading = true;

    this.studyService.getClientsByParentGroup(Number(this.study.clientGroup)).subscribe(
      res => {
        this.clients = res;
        this.clients.sort(function (a, b) {
          if (a['rcnShortName'] < b['rcnShortName']) return -1;
          else if (a['rcnShortName'] > b['rcnShortName']) return 1;
          return 0;
        });
        this.clients.unshift({
          "rcnShortName": "Multiple",
          "rcnId": "multi"
        })
        this.clients.unshift({
          "rcnShortName": "Other",
          "rcnId": "other"
        })
        // this.clients.unshift({
        //   "rcnShortName": "",
        //   "rcnId": ""
        // })
        this.clientState.error = false;
        this.clientState.loaded = true;
        this.clientState.loading = false;
        if (this.study.clientGroup == "multi" || this.study.clientGroup == "other")
          this.study.client = this.study.clientGroup
        else
          this.study.client = ""

            ;
        console.log(this.clients);

        this.onClientChanged()
      }, err => {
        this.clients = [];
        this.clientState.error = true;
        this.clientState.loaded = true;
        this.clientState.loading = false;;
        this.onClientChanged()
      }
    )
  }

  showMultiClient() {
    if (this.study && this.study.clientGroup == "multi") return true
    return false
  }

  getStatus(value: string) {
    if (!value) return ""

    switch (value.trim()) {
      case "progress":
        return "In Progress";
      default:
        return value.charAt(0).toUpperCase() + value.slice(1);
    }
  }

  addBusiness(bId, e) {
    e.target.value = ""
    if (!Array.isArray(this.business) || bId == "")
      return
    if (!this.study.lineOfBuisness.find(be => be.rlobId == bId)) {
      this.study.lineOfBuisness.push(
        this.business.find(be => be.rlobId == bId)
      )
    }
  }

  removeBusiness(b) {
    this.study.lineOfBuisness.splice(
      this.study.lineOfBuisness.findIndex(be => be.rlobId == b.rlobId), 1
    )
  }

  loading = false
  isLoading() {
    return this.loading;
  }

  canDeleteStudy() {
    if (!this.study.id)
      return false
    let timeStampNow = Math.floor(Date.now());
    let difference = timeStampNow - this.study.dateLastFlagModification;
    let daysDifference = Math.floor(difference / 1000 / 60 / 60 / 24);
    //console.log(daysDifference)
    return this.study.status.trim() == "progress" || (this.study.status.trim() == "cancelled" && daysDifference >= 180)
  }

  message = {}

  deleteClick(content) {
    this.message = { title: "Warning", msg: "Do you really want to delete the study ?", icon: "delete", confirm: () => { this.confirmDeleteion() } }
    this.modalService.open(content)

  }

  cancelClick(content) {
    this.message = { title: "Warning", msg: "Do you really want to cancel the study ?", icon: "cancel", confirm: () => { this.confirmCancelling() } }
    this.modalService.open(content)
  }

  activateClick(content) {
    this.message = { title: "Warning", msg: "Do you really want to activate the study ?", icon: "activate", confirm: () => { this.confirmActivating() } }
    this.modalService.open(content)
  }



  confirmDeleteion() {
    //console.log(this.deleteDataCheck)
    this.studyService.deleteStudy(this.study.id, this.deleteDataCheck).subscribe(
      res => {
        this.delete.emit(this.study.id + " is deleted")
        this.tsr.pop("success", "Deleted", "The study has been deleted successfully")
      },
      err => {
        this.tsr.pop("error", "Error", "Study not deleted")
      }
    )
  }

  confirmCancelling() {
    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }
    this.studyService.changeStudyStatus(this.study.id, "cancelled", this.us.getStoredUser().ruId).subscribe(
      res => {
        this.study.lastStatusModificationDate = res["stLastStatusModificationDate"] ? datef(res["stLastStatusModificationDate"]) : null
        this.study.lastStatusModifiedBy = this.us.getStoredUser()
        this.study.status = "Cancelled"
        this.statusChange.emit("Cancelled")
        this.tsr.pop("success", "Cancelled", "The study has been cancelled successfully")
      },
      err => {
        this.tsr.pop("error", "Error", "Study not cancelled")
      }
    )
  }

  confirmActivating() {
    let datef = (format: string): date => {
      if (!format) return null
      let obj = format.split("-")
      return new date(obj[0], obj[1], obj[2])
    }
    this.studyService.changeStudyStatus(this.study.id, "validated", this.us.getStoredUser().ruId).subscribe(
      res => {
        this.study.status = "Validated"
        this.study.lastStatusModifiedBy = this.us.getStoredUser()
        this.study.lastStatusModificationDate = res["stLastStatusModificationDate"] ? datef(res["stLastStatusModificationDate"]) : null
        this.statusChange.emit("Validated")
        this.tsr.pop("success", "Activated", "The study has been activated successfully")
      },
      err => {
        this.tsr.pop("error", "Error", "Study not cancelled")
      }
    )
  }

  isStudyValidated() {
    if (!this.study.id)
      return false
    return this.study.status.toLocaleLowerCase().trim() == "validated"
  }

  isStudyCancelled() {
    if (!this.study.id)
      return false
    return this.study.status.toLocaleLowerCase().trim() == "cancelled"
  }

  canEdit() {
    if (this.isStudyValidated())
      return false
    if (this.permissionsService.getPermission("STUDY_WRITER") || (!this.study.id && this.permissionsService.getPermission("PRODUCER")))
      return true
    return false
  }

  canEditDataQualityAndComment() {
    if (this.permissionsService.getPermission("STUDY_WRITER") || (!this.study.id && this.permissionsService.getPermission("PRODUCER")))
      return true
    return false
  }

  deleteStudyDoc() {
    this.studyService.deleteFile(this.study.attachedFilePath, this.study.id).subscribe(res => {
      this.study.attachedFilePath = ""
      this.percentDone = -1
      this.tsr.pop("success", "Success", "The study documentation has been deleted successfully")
    }, err => {
      this.study.attachedFilePath = ""
      this.percentDone = -1
      this.tsr.pop("error", "Error", "Error while deleting study documentation")
    })

  }

  onTreatyChanged() {
    console.log(this.study.treatyNumber);
   // this.clients = this.Allclients
    let treaty = this.treaties.find(treaty => treaty.rtId == this.study.treatyNumber)
    console.log(treaty);
    this.study.client = treaty.refCedentNameByRtRcnId.rcnId
    this.onClientChanged(treaty.rtId)

  }
  matchStudyRequester(requesterId) {
    if (this.clienttypes && Array.isArray(this.clienttypes) && this.clienttypes.length > 0) {
      let requester = this.clienttypes.find(c => c.stcId == requesterId)
      if (requester)
       return requester.stcName
      return "" 
    }
    return ""
  }
  matchStudyDatasource(datasourceId) {
    if (this.datasources && Array.isArray(this.datasources) && this.datasources.length > 0) {
      let requester = this.datasources.find(c => c.rdsId == datasourceId)
      if (requester)
       return requester.rdsName
      return "" 
    }
    return ""
  }
  matchStudyEngine(engineId) {
    if (this.engines && Array.isArray(this.engines) && this.engines.length > 0) {
      let requester = this.engines.find(c => c.rcetId == engineId)
      if (requester)
       return requester.rcetName
      return "" 
    }
    return ""
  }
  matchStudyClient(clientId) {
    if (this.clients && Array.isArray(this.clients) && this.clients.length > 0) {
      let requester = this.clients.find(c => c.rcnId == clientId)
      if (requester)
        return requester.rcnShortName
      return ""  
    }
    return ""
  }
  matchStudyClientGroup(clientGroupId) {
    if (this.groups && Array.isArray(this.groups) && this.groups.length > 0) {
      let requester = this.groups.find(c => c.rpgId == clientGroupId)
      if (requester)
       return requester.rpgName
      return "" 
    }
    return ""
  }
  matchStudyClientCountry(clientCountryId) {
    if (this.countries && Array.isArray(this.countries) && this.countries.length > 0) {
      let requester = this.countries.find(c => c.rcId == clientCountryId)
      if (requester)
       return requester.rcName
      return "" 
    }
    return ""
  }
  matchStudyClientTreaty(treatyId) {
    let client = this.matchStudyClient(this.study.client)
    if (client && client.trim().toLowerCase() == "multiple") return "Multiple"
    if (this.treaties && Array.isArray(this.treaties) && this.treaties.length > 0) {
      let requester = this.treaties.find(c => c.rtId == treatyId)
      if (requester && requester.rtName) return requester.rtName

    }
    return ""
  }



}
