import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DashboardModel } from '../model/dashboard-model';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DashboardServiceService {
    private  _apiURL :string= "http://192.168.102.92:8111/dashboard/";
    private _token = sessionStorage.getItem('token')

    constructor(private _httpClient: HttpClient) { }

    getDashboardData():Observable<DashboardModel>{ 
        return this._httpClient.get<DashboardModel>(this._apiURL,{  headers: new HttpHeaders({token: this._token!})})
    }
}
