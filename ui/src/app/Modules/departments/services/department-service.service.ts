import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { DepartmentModel } from '../Model/department.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';

export interface departmentDataInterface {
    departmentName: string;
    departmentCode: string;
    skills: string;
    headedBy: string;
}

@Injectable({
    providedIn: 'root',
})
export class DepartmentServiceService {
    private _apiURL: string = 'http://192.168.102.92:8111/department/';
    token: any;
    departmentData = new BehaviorSubject<any>([]);

    constructor(private _httpClient: HttpClient) {
        this.token = sessionStorage.getItem('token');
    }

    getDepartmentData(): Observable<DepartmentModel[]> {
        return this._httpClient.get<DepartmentModel[]>(
            this._apiURL + 'getAllDepartmentsList',
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    getSingleDeptData(id: number): Observable<DepartmentModel> {
        return this._httpClient.get<DepartmentModel>(
            this._apiURL + `getDepartmentDetails/${id}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    // Adds Department
    addDepartment(departmentData: any): Observable<any> {
        return this._httpClient.post<any>(
            this._apiURL + 'saveDepartmentDetails',
            departmentData,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    // Update Department
    updateDepartment(
        departmentData: departmentDataInterface
    ): Observable<departmentDataInterface> {
        return this._httpClient.put<any>(
            this._apiURL +'updateDepartmentDetails',
            departmentData,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    deleteDepartment(departmentId: number): Observable<any> {
        return this._httpClient.put<any>(
            this._apiURL + `deleteDepartmentDetails/${departmentId}`,
            {},
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    getAllTeams(departmentId: number): Observable<any> {
        return this._httpClient.get<any>(
            `http://192.168.102.92:8111/team/getAllTeams/${departmentId}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }
}
