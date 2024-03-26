export interface UserData{
    userId: number,
    empCode: string,
    email: string,
    priviledgeId: string,
    createdDate: string,
    modifiedDate: string,
    active: boolean
}

export interface newUser{
     email: string,
      priviledgeId: number
      password: string,
      empCode:string
}

export interface editUserData{
    
        email:string
        password:string,
        priviledgeId:number
      
}