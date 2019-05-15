export class RunTable
{
    id: number
    name: string

    clone (){
        let obj = new RunTable()
        obj.name = this.name
        return obj;
    }

}