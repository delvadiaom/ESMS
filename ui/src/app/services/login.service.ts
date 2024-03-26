import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';


export interface LoginResponse {
    token: string,
    userId: number,
    empCode: string,
    email: string,
    priviledgeId: string,
    createdDate: string,
    modifiedDate: string,
    active: boolean
}

export interface userCred {
    username: string,
    password: string
}
@Injectable({
    providedIn: 'root'
})


export class LoginService {

    userLogin: boolean = true;
    userdata = new BehaviorSubject<any>("Hello");

    constructor(private httpClient: HttpClient) { }

    login(usercreditionals: userCred): Observable<LoginResponse> {
        console.log(usercreditionals)
        return this.httpClient.post<LoginResponse>("http://192.168.102.92:8111/auth/login", usercreditionals);
    }

    forgotpassword(email: string): Observable<any> {
        return this.httpClient.post<any>("http://192.168.102.92:8111/auth/forgotPassword", {
            mail: email
        });
    }

    verifyOtp(usercreditionals: { mail: string, otp: number }): Observable<any> {
        console.log("Inside API");
        console.log(usercreditionals);
        return this.httpClient.post<any>("http://192.168.102.92:8111/auth/verifyOtp", usercreditionals);
    }

    setNewPassword(usercreditionals: { mail: string, otp: number, password: string }): Observable<any> {
        return this.httpClient.post<any>("http://192.168.102.92:8111/auth/setPasswordWithOtp", usercreditionals);
    }

    validateToken(token: any): Observable<LoginResponse> {
        return this.httpClient.get<LoginResponse>("http://192.168.102.92:8111/auth/validateToken", {
            headers: new HttpHeaders({ token: token })
        })
    }

}
