import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ExperinceWiseSalaryModel, TeamWiseCosting } from '../Model/reports.model';

export interface department {
    departmentName: string;
    departmentCode: string;
    headedBy: string;
    headedByName: string;
    costing: string;
    status: null;
    ctc: null;
    active: boolean;
}

export interface ExpCosting {
    employeeCode: string;
    employeeName: string;
    teamName: string;
    roleName: string;
    empType: string;
    experience: string;
    ctc: number;
}

@Injectable({
    providedIn: 'root',
})
export class ReportServiceService {
    constructor(private _httpClient: HttpClient) { }
    private _token:any = sessionStorage.getItem('token');
    private _baseURL: string = 'http://192.168.102.92:8111/report';
    private _experinceGraphURL = this._baseURL + '/getEmployeeExperienceGraph';
    private _teamWiseCostUrl = this._baseURL + '/getTeamGraph/';

    getExperienceWiseSalaryChart(): Observable<ExperinceWiseSalaryModel[]> {
        return this._httpClient.get<ExperinceWiseSalaryModel[]>(
            this._experinceGraphURL,
            {
                headers: new HttpHeaders({ token: this._token }),
            }
        );
    }
    getTeamWiseCostChart(departmentId: string): Observable<TeamWiseCosting[]> {
        return this._httpClient.get<TeamWiseCosting[]>(
            this._teamWiseCostUrl + departmentId,
            {
                headers: new HttpHeaders({ token: this._token }),
            }
        );
    }
    getDepartmnetWiseCosting(): Observable<any> {
        return this._httpClient.get<any>(
            'http://192.168.102.92:8111/report/getCostingByDepartment/',
            {
                headers: new HttpHeaders({ token: this._token }),
            }
        );
    }

    getAllDepartmentReport(): Observable<any> {
        return this._httpClient.get<any>('http://192.168.102.92:8111/report/getDepartmentReport', {
            headers: new HttpHeaders({ token: this._token }),
        });
    }
    getIndividualDeparment(pageNumber: number, pageSize: number): Observable<any> {
        return this._httpClient.get<any>(this._baseURL + `getEmployeeReport/${pageNumber}/${pageSize}`, {
            headers: new HttpHeaders({ token: this._token }),
        });
    }
}
