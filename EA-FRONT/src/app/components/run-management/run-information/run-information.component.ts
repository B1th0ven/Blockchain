import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  OnDestroy
} from '@angular/core';
import {
  Run
} from '../../../shared/models/run';
import {
  RunService
} from '../../../services/run.service';
import {
  Subscription,
  Subject
} from 'rxjs';
import {
  interval
} from 'rxjs/observable/interval';

@Component({
  selector: 'app-run-information',
  templateUrl: './run-information.component.html',
  styleUrls: ['./run-information.component.scss']
})
export class RunInformationComponent implements OnInit, OnDestroy {

  @Input() run: Run

  @Output() done:EventEmitter<any> = new EventEmitter()

  sub: Subscription
  subsub: Subscription

  private readonly getSatus = () => {
    this.subsub = this.rs.getRunStatus(this.run.id)
      .subscribe(run_calc => {
        //console.log("STATUS CALLED FOR RUN ", this.run.id, "---> ",run_calc);
        this.run.run_calc = run_calc;
        if (run_calc) {
          let status = this.getRunStatus(this.run.run_calc.rclcStatus,this.run.run_calc.rclcStatusEa)
          console.log(status);
          
          if(status){
            switch (status.toUpperCase()) {
              case "PREPARING CALCULATION":
              case "CALCULATION IN PROGRESS":
              case "SYNCHRONISING RESULTS":
              case "CALCULATION TERMINATED":
                break;


              case "DONE":
                this.done.emit()
              case "ERROR":
              case "ABORTED":
              case "CALCULATION ERROR":
              default:
                this.sub.unsubscribe();
                this.subsub.unsubscribe();
                break;
            }
          } else {
            this.sub.unsubscribe();
            this.subsub.unsubscribe();
          }
        }else{
          this.sub.unsubscribe();
          this.subsub.unsubscribe();
        }
      });
  };

  constructor(private rs: RunService) {}

  ngOnInit() {
    if (this.run) {

      let timeout
      let timespan = 5000

      this.getSatus()
      this.sub = interval(timespan).subscribe(this.getSatus)
    }
  }

  ngOnDestroy() {
    console.log("UNSUB")
    if (this.sub) this.sub.unsubscribe()
    if (this.subsub) this.subsub.unsubscribe()
  }

  // getColor() {
  //   if (!this.run.run_calc) return 'secondary';

  //   switch (String(this.run.run_calc.rclcStatus).toUpperCase()) {
  //     case 'DONE':
  //       return 'success';
  //     case 'IN PROGRESS':
  //       return 'warning';
  //     case 'ERROR':
  //     case 'ABORTED':
  //     case 'ERROR WHILE SYNCHRONIZING RESULT':
  //       return 'danger';
  //     default:
  //       return 'info'
  //   }
  // }

  // getColorEa() {
  //   if (!this.run.run_calc) return 'secondary';

  //   switch (String(this.run.run_calc.rclcStatusEa).toUpperCase()) {
  //     case 'DONE':
  //       return 'success';
  //     case 'IN PROGRESS':
  //       return 'warning';
  //     case 'ERROR':
  //     case 'ABORTED':
  //     case 'ERROR WHILE SYNCHRONIZING RESULT':
  //       return 'danger';
  //     default:
  //       return 'info'
  //   }
  // }

  getDate(str) {
    return (str) ? new Date(str) : null
  }


  getRunStatus(sasStatus,eaStatus) {
    return this.rs.getStatus(sasStatus,eaStatus)
  }

  getColors(status) {
    if (!status || status == "") return 'secondary';
    switch (status.toUpperCase()) {
      case 'DONE':
        return 'success';
      case "CALCULATION IN PROGRESS":
      case "SYNCHRONISING RESULTS":
        return 'warning';
      case 'ERROR':
      case 'ABORTED':
      case 'ERROR WHILE SYNCHRONIZING RESULT':
      case "CALCULATION ERROR":
        return 'danger';
      default:
        return 'info'
    }
  }



}
