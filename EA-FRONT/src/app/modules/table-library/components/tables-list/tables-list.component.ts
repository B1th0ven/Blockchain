import { Component, OnInit, EventEmitter, Output, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';
import { Tab } from '../../models/tab.class';
import { TAB_TYPE } from '../../enums/tab.enum';
import { TablesService } from '../../services/tables.service';
import { ExpectedTable } from '../../models/expected-table.model';
import { ToasterService } from 'angular5-toaster/dist/angular5-toaster';
import { NgxPermissionsService } from '../../../../../../node_modules/ngx-permissions';
import { UsersService } from '../../../../services/users.service';
import { DateFormatPipe } from '../../../../shared/pipes/date-format.pipe';

@Component({
  selector: 'app-tables-list',
  templateUrl: './tables-list.component.html',
  styleUrls: ['./tables-list.component.scss']
})
export class TablesListComponent implements OnInit, OnDestroy {

  @Output() newTabEvent:EventEmitter<any> = new EventEmitter()

  query ={
    country:"",
    name:"",
    version:"",
    decrement:"",
    type:"",
    origin:"",
    application:"",
    publication:"",
    source:"",
    code:"",
    creation_date :"",
  }
  pagesize = 20
  selectedpage = 1
  sortKey = "creation_date";
  sortIcon = "sort-up"
  desc = true;

  tables:ExpectedTable[]
  busy:boolean = false
  err:any = null

  constructor(private ts: TablesService,private tsr:ToasterService,private permissionsService: NgxPermissionsService,private us:UsersService, private df: DateFormatPipe) { }


  items=[{id:1,text:"eh"},{id:2,text:"4eh"},{id:3,text:"e8h"},{id:4,text:"eh1"}]

  ngOnInit() {
    console.log("from table lib",this.us.getStoredUser())
    this.loadSearchCriteria()

    this.busy=true;
    this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc);
  }

  ngOnDestroy(): void {
    this.saveSearchCriteria()
  }


  newTab(open,type, table ?) {
    let t;
      if(type == 'create')
      t = TAB_TYPE.CREATE
      else t = TAB_TYPE.MODIFY
      this.newTabEvent.emit({
        type: t,
        name: (table)?table.name:"New Table",
        entity:table,
        open:open
      }) 
   
  }

  grayed(t:any){
    if (t.status.trim() == "inactive" || !t.latest_version) {
      return true
    } else return false 
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

  asychRequest
  totalpages
  paginate(page, size, q, sort, desc) {
    this.busy = true
    this.err = null;

    //PREVENT CONCURRENT API CALLS
    if (this.asychRequest)
      this.asychRequest.unsubscribe()

    this.asychRequest = this.ts.getPage(page - 1, size, q, sort, desc).subscribe(
      data => {
        this.busy = false;
        this.tables = data["content"]
        this.selectedpage = page
        this.totalpages = data["totalPages"]
        //this.updatePager()

      },
      err => {
        this.tables = null;
        this.busy = false;
        this.tsr.pop("error","Error","Server Error")
        this.err = "Could retrieve page!"
      }
    )
  }

  setSortIcon = (key): string => {
    if (this.sortKey == key) {
      if (this.desc) return "sort-down"
      else return "sort-up"

    } else return "";
  }


  typingTimeout = null;
  filter = () => {
    clearTimeout(this.typingTimeout)
    this.typingTimeout = setTimeout(() => {
      this.paginate(1, this.pagesize, this.query, this.sortKey, this.desc);
    }, 400);
  }

  onSelectPage(page){this.selectedpage=page;this.paginate(this.selectedpage, this.pagesize, this.query, this.sortKey, this.desc);}
  onSelectPageSize(size){this.pagesize=size;this.paginate(1, this.pagesize, this.query, this.sortKey, this.desc);}

  private saveSearchCriteria()
  {
    let critertia = {
      query:this.query,
      desc:this.desc,
      sortIcon:this.sortIcon,
      sortKey:this.sortKey,
      pagesize:this.pagesize,
      selectedpage:this.selectedpage
    }
    sessionStorage.setItem("table_management",JSON.stringify(critertia))
  }

  private loadSearchCriteria()
  {
    let criteria = JSON.parse( sessionStorage.getItem("table_management"))

    if (criteria)
    {
      this.query = criteria.query,
      this.desc = criteria.desc,
      this.sortIcon = criteria.sortIcon,
      this.sortKey = criteria.sortKey,
      this.pagesize = criteria.pagesize,
      this.selectedpage = criteria.selectedpage
    }
  }

  formatDateString(date: Date) {
    if (!date) return ""
    let str : string = date.toLocaleString();
    str = str.split(" ")[0];
    return str;
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
      case "decrement":
        if(!this.query.decrement || this.query.decrement == "") {
          isBlank = true
        }
        this.query.decrement = ""
        break;
      case "type":
        if(!this.query.type || this.query.type == "") {
          isBlank = true
        }
        this.query.type = ""
        break;
      case "name":
        if(!this.query.name || this.query.name == "") {
          isBlank = true
        }
        this.query.name = ""
        break;
      case "version":
        if(!this.query.version || this.query.version == "") {
          isBlank = true
        }
        this.query.version = ""
        break;
      case "origin":
        if(!this.query.origin || this.query.origin == "") {
          isBlank = true
        }
        this.query.origin = ""
        break;
      case "application":
        if(!this.query.application || this.query.application == "") {
          isBlank = true
        }
        this.query.application = ""
        break;
      case "publication":
        if(!this.query.publication || this.query.publication == "") {
          isBlank = true
        }
        this.query.publication = ""
        break;
      case "source":
        if(!this.query.source || this.query.source == "") {
          isBlank = true
        }
        this.query.source = ""
        break;
      case "creation_date":
        if(!this.query.creation_date || this.query.creation_date == "") {
          isBlank = true
        }
        this.query.creation_date = ""
        break;
      
      default:
        break;
    }
    if(!isBlank) {
      this.filter()
    }
  }
}
