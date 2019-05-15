import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  OnDestroy
} from '@angular/core';
import {
  Study
} from '../../shared/models/study';
import {
  StudyService
} from '../../services/study.service';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';
import { Subscription } from 'rxjs';
import { NgxPermissionsService } from '../../../../node_modules/ngx-permissions';
import { UsersService } from '../../services/users.service';

@Component({
  selector: 'app-study-list-search',
  templateUrl: './study-list-search.component.html',
  styleUrls: ['./study-list-search.component.scss']
})
export class StudyListSearchComponent implements OnInit, OnDestroy {

  asychRequest: Subscription;
  @Output() newTabEvent: EventEmitter < any > = new EventEmitter < any > ()
  pages: number[]
  selectedpage: number = 1
  pagesize: number = 20
  totalpages: number

  studies: Array < any >

    query = {
      code: "",
      country: "",
      group: "",
      client: "",
      brand: "",
      treaty: "",
      lob:"",
      status: "",
      statusdate: "",
      createdBy: ""
    }

  sortKey = "statusdate";
  sortIcon = "sort-down"
  desc = true;

  err: string
  busy: boolean = false

  lobs: Array<any>
  selectedLobs = null

  constructor(private studyService: StudyService, private tsr: ToasterService,private permissionsService: NgxPermissionsService,private us: UsersService) {}

  ngOnInit() {
    this.init();
  }

  private getCache() {
    this.query = JSON.parse(sessionStorage.getItem("study_management_query")) || this.query;
    this.selectedpage = JSON.parse(sessionStorage.getItem("study_management_selected_page")) || this.selectedpage;
    this.pagesize = JSON.parse(sessionStorage.getItem("study_management_page_size")) || this.pagesize;
    this.selectedLobs = JSON.parse(sessionStorage.getItem("study_management_lobs")) || this.selectedLobs;
    let sortInfo = JSON.parse(sessionStorage.getItem("study_management_sort_info"));
    if (sortInfo) {
      this.sortIcon = sortInfo.sortIcon;
      this.desc = sortInfo.desc;
      this.sortKey = sortInfo.sortKey;
    }
  }

  private init() {

    this.getCache();

    this.err =null
    this.busy = true;
    this.studyService.getLobs().subscribe(res => {
      this.lobs = res;
      console.log("-----------------lobs",this.lobs)

      this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc);
    }, err => {
      this.studies = null;
      this.busy = false;
      this.tsr.pop("error", "Error", "Server Error");
      this.err = "Server Error";
    });
  }

  ngOnDestroy() {
    if (this.asychRequest)
      this.asychRequest.unsubscribe()

    sessionStorage.setItem("study_management_query", JSON.stringify(this.query))
    sessionStorage.setItem("study_management_selected_page", JSON.stringify(this.selectedpage))
    sessionStorage.setItem("study_management_page_size", JSON.stringify(this.pagesize))
    sessionStorage.setItem("study_management_lobs", JSON.stringify(this.selectedLobs))
    sessionStorage.setItem("study_management_sort_info", JSON.stringify({
      sortIcon:this.sortIcon,
      sortKey:this.sortKey,
      desc:this.desc
    }))

  }

  newTab(open,type, study ?) {
    
    
    this.newTabEvent.emit({
      type: type,
      param: study,
      open:open
    })


  
    
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

  formatDateString(str: String) {
    if (!str) return ""
    let arr = str.split("-");
    if (arr.length != 3) return "Corrupt Data"
    return arr.reverse().join("/")
  }

  getVMO(val, mult, oth) {
    if (mult && !oth) return "Multiple"
    if (oth && !mult) return "Other"
    if (val && !oth && !mult) return val
    return ""
  }

  paginate(page, size, q, sort, desc) {
    this.busy = true
    this.err = null;

    //PREVENT CONCURRENT API CALLS
    if (this.asychRequest)
      this.asychRequest.unsubscribe()

    this.asychRequest = this.studyService.getPage(page - 1, size, q, sort, desc).subscribe(
      data => {

        this.busy = false;
        this.studies = data["content"]
        console.log(this.studies)
        this.selectedpage = page
        this.totalpages = data["totalPages"]
        this.updatePager()
      },
      err => {
        this.studies = null;
        this.busy = false;
        this.tsr.pop("error","Error","Server Error")
        this.err = "Could not retrieve page!"
      }
    )
  }

  pagesizechange(val) {
    this.pagesize = val;
    this.paginate(1, this.pagesize, this.query, this.sortKey, this.desc)
  }

  updatePager() {
    if (this.totalpages <= 10)
      this.pages = Array(this.totalpages).fill(0).map((e, i) => i + 1)
    else {
      if (this.selectedpage <= 6)
        this.pages = Array(10).fill(0).map((e, i) => i + 1)
      else if (this.selectedpage + 4 >= this.totalpages) {
        let startPage = this.totalpages - 9;
        this.pages = Array(10).fill(0).map((_, idx) => startPage + idx)
      } else {
        let startPage = this.selectedpage - 5;
        this.pages = Array(10).fill(0).map((_, idx) => startPage + idx)

      }

    }
  }

  typingTimeout = null;
  filter = () => {
    clearTimeout(this.typingTimeout)
    this.typingTimeout = setTimeout(() => {
      this.paginate(1, this.pagesize, this.query, this.sortKey, this.desc);
    }, 400);
  }

  changeSortKey(key, desc?) {
    if (this.sortKey == key)
      this.desc = !this.desc
    else
      this.desc = (desc)?desc:false;

    this.sortKey = key;
    this.sortIcon = this.setSortIcon(key)
    this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc)
  }

  setSortIcon = (key): string => {
    if (this.sortKey == key) {
      if (this.desc) return "sort-down"
      else return "sort-up"

    } else return "";
  }


dropdownSettings = {
        singleSelection: false,
        idField: 'rlobId',
        textField: 'rlobName',
        selectAllText: 'All',
        unSelectAllText: 'None',
        itemsShowLimit: 0,
        allowSearchFilter: false
    };

  onItemSelect($event)
  {
      this.query.lob = (this.selectedLobs)? this.selectedLobs.map(e=>e["rlobName"].trim()).join(";"):"";
      this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc)
  }
  onSelectAll()
  {
    this.query.lob = this.lobs.join(";")
    this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc)
  }
  onDeSelectAll()
  {
    this.query.lob = ""
    this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc)
  }

  nextPage()
  {
    this.selectedpage = Math.min(this.selectedpage+1, this.pages.length)
  }

  previousPage()
  {
    this.selectedpage = Math.max(this.selectedpage-1, 1)
  }

  concatinate(a,b) {
    if(a && b)
    return a.toUpperCase() + " " + b.toUpperCase()
    else return ""
  }

  clearSearchField(field) {
    let isBlank = false
    switch (field) {
      case "code":
        if(!this.query.code || this.query.code == "") {
          isBlank = true
        }
        this.query.code = ""
        break;
      case "country":
        if(!this.query.country || this.query.country == "") {
          isBlank = true
        }
        this.query.country = ""
        break;
      case "brand":
        if(!this.query.brand || this.query.brand == "") {
          isBlank = true
        }
        this.query.brand = ""
        break;
      case "group":
        if(!this.query.group || this.query.group == "") {
          isBlank = true
        }
        this.query.group = ""
        break;
      case "client":
        if(!this.query.client || this.query.client == "") {
          isBlank = true
        }
        this.query.client = ""
        break;
      case "treaty":
        if(!this.query.treaty || this.query.treaty == "") {
          isBlank = true
        }
        this.query.treaty = ""
        break;
      case "createdBy":
        if(!this.query.createdBy || this.query.createdBy == "") {
          isBlank = true
        }
        this.query.createdBy = ""
        break;
      case "status":
        if(!this.query.status || this.query.status == "") {
          isBlank = true
        }
        this.query.status = ""
        break;
      case "statusdate":
        if(!this.query.statusdate || this.query.statusdate == "") {
          isBlank = true
        }
        this.query.statusdate = ""
        break;
    
      default:
        break;
    }
    if(!isBlank) {
      this.filter()
    }
  }

}
