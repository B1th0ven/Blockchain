export class date {
  year: number
  month: number
  day: number

  constructor(year ? , month ? , day ? ) {
    this.day = (day) ? Number(day) : null
    this.month = (month) ? Number(month) : null
    this.year = (year) ? Number(year) : null
  }

  public static formatDate(date: date, format ? : string) {

    if (!date || !date.year || !date.month || !date.day) return null

    // var d = new Date(date.year + "-" + (Number(date.month)) + "-" + date.day),
      let month = '' + Number(date.month)
      let day = '' + date.day
      let year = '' + date.year;

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    switch (format) {
      case "dd/mm/yyyy": return [day, month, year].join('/');
      case "dd/mm": return [day,month].join('/');
      default: return [year, month, day].join('-');
    }
  }

  public valid() {
    if (isNaN(this.year) || isNaN(this.day) || isNaN(this.month)) return false
    return true
  }

  public static validFormat(indate)
  {
    let y = String(indate.year).trim().length
    let m = String(indate.month).trim().length
    let d = String(indate.day).trim().length
    if ( y != 4 ) return false;
    if ( m < 1 || m > 2 ) return false;
    if ( d < 1 || d > 2 ) return false;
    return true;
  }

  public static valid(indate: date) {
    if (!indate) return false
    if (!indate.year || !indate.day || !indate.month) {
      return false
    }

    if (isNaN(indate.year) || isNaN(indate.day) || isNaN(indate.month)) {

      return false
    } else {

      return true
    }

  }

  public static dateFromString(st: string) {
    if (!st) return null
    let obj = st.split("-")
    if (obj.length != 3) return null
    return new date(obj[0], obj[1], obj[2])
  }

  public static getCurrentDate(date : Date){
    
    let  dd:any = date.getDate();
    let mm:any = date.getMonth()+1; 
    let yyyy:any = date.getFullYear();
    

    if(dd<10) {
    dd = '0'+dd
      } 

    if(mm<10) {
    mm = '0'+mm
    }  

  return  dd + '/' + mm + '/' + yyyy;
  }

  public static getCurrentTime(date : Date){
    let hours:any = date.getHours();
    let minutes:any = date.getMinutes()
    let seconds:any =date.getSeconds()
    if(hours<10) hours = '0'+hours
    if(minutes<10) minutes = '0'+minutes
    if(seconds<10) seconds = '0'+seconds

    return hours + ":"  
    + minutes + ":" 
    + seconds;
  }

  public toString() {
    let  dd:any = this.day;
    let mm:any = this.month; 
    let yyyy:any = this.year;
    

    if(dd<10) {
    dd = '0'+dd
      } 

    if(mm<10) {
    mm = '0'+mm
    }  

  return  dd + '/' + mm + '/' + yyyy;
  }
}
