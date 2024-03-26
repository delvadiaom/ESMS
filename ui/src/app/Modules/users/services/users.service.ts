import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {  newUser } from '../Model/UserData.model';
import { EmpData } from '../../employee/models/EmpData.model copy';

@Injectable({
    providedIn: 'root',
})
export class UsersService {
    private _apiUrl = 'http://192.168.102.92:8111';
    private _getAllUsers = this._apiUrl + '/user/getAllUserDetails';
    private _token = sessionStorage.getItem('token');
    private _getEmpFromEmailId = this._apiUrl + '/employee/getEmpCodeFromEmail';

    constructor(private http: HttpClient) {
        this.getAllRols();
    }

    // Fetching all the users
    getAllUsers(): Observable<any> {
        return this.http.get(this._getAllUsers, {
            headers: new HttpHeaders({ token: this._token! }),
        });
    }

    // Fetching Employee data by email
    fetchDataFromEmail(emailId: string): Observable<EmpData[]> {
        return this.http.get<EmpData[]>(
            'http://192.168.102.92:8111/employee/getEmployeeDetailsFromEmail' +
                `/${emailId}`,
            {
                headers: new HttpHeaders({ token: this._token! }),
            }
        );
    }

    // Adding user new user
    addUser(data: newUser): Observable<any> {
        console.log('in service', data);

        return this.http.post<any>(
            'http://192.168.102.92:8111/user/saveUserDetails',
            data,
            {
                headers: new HttpHeaders({ token: this._token! }),
            }
        );
    }

    // Deleting existing user
    deleteUser(data: any): Observable<any> {
        console.log('User to be deleted: ', data);

        return this.http.put(
            'http://192.168.102.92:8111/user/deleteUserDetails/' + data.userId,
            {},
            {
                headers: new HttpHeaders({ token: this._token! }),
            }
        );
    }

    // Editing user
    editUser(data: any): Observable<any> {
        return this.http.put(
            'http://192.168.102.92:8111/user/updateUserDetails',
            data,
            {
                headers: new HttpHeaders({ token: this._token! }),
            }
        );
    }

    // Fetching all the roles
    getAllRols() {
        let allRoles: any[] = [];
        return this.http.get(
            'http://192.168.102.92:8111/user/getPriviledgeDetails',
            {
                headers: new HttpHeaders({ token: this._token! }),
            }
        );
    }
}
