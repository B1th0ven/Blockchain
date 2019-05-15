export class User
{
    id: number
    name: string
    lastname: string
    login:string
    role:string
    email:string
    function:string
    scope:any
    country:any
    market:any
    saveLoader:boolean = false
    isChanged:boolean = false
    constructor(user?){
        if(user)
        this.mapFromApi(user)
    }

     mapFromApi(user){
        this.id = user.ruId
        this.login = user.ruLogin
        this.name = user.ruFirstName
        this.lastname = user.ruLastName
        this.role = user.ruRole
        this.function = user.ruFunction
        this.scope = user.scope
        this.country = user.country
        this.market = user.market
        this.email = user.ruMailAdresse

    }

    mapToApi(){
        let obj = {
            ruId:this.id,
            ruLogin:this.login,
            ruFirstName:this.name,
            ruLastName:this.lastname,
            ruRole:this.role,
            ruFunction:this.function,
            scope:this.scope,
            country:this.country,
            market:this.market,
            ruMailAdresse : this.email
        }

        return obj
    }

    jsonToUser(user){
        this.id = user.id
        this.login = user.login
        this.name = user.name
        this.lastname = user.lastname
        this.role = user.role
        this.function = user.function
        this.scope = user.scope
        this.country = user.country
        this.market = user.market
        this.email = user.email
    }
    set(user) {
        this.id = user.id
        this.login = user.login
        this.name = user.name
        this.lastname = user.lastname
        this.role = user.role
        this.function = user.function
        this.scope = user.scope
        this.country = user.country
        this.market = user.market
        this.email = user.email
    }
}