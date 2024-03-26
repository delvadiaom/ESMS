import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmpData } from '../models/EmpData.model copy';
import { EmpBasicDetails } from '../models/EmpBasicDetails.model';

@Injectable({
    providedIn: 'root',
})
export class EmployeeServiceService {
    token: any = sessionStorage.getItem('token');
    constructor(private httpClient: HttpClient) {}

    fetchEmpsData(pageNumber: number, pageSize: number): Observable<EmpData[]> {
        return this.httpClient.get<EmpData[]>(
            `http://192.168.102.92:8111/employee/getAllEmployeeDetails/${pageNumber+1}/${pageSize}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    fetchEmpsCount(): Observable<number> {
        return this.httpClient.get<number>(
            `http://192.168.102.92:8111/employee/getCountOfEmployee`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    fetchEmpsDataWithTeamId(teamId: number): Observable<EmpData[]> {
        return this.httpClient.get<EmpData[]>(
            `http://192.168.102.92:8111/employee/getAllEmployeeDetailsByTeamId/${teamId}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    fetchSingleEmpBasic(empId: Number): Observable<EmpBasicDetails> {
        return this.httpClient.get<EmpBasicDetails>(
            `http://192.168.102.92:8111/employee/getEmployee/${empId}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    addEmployeeDetails(
        adminCode: string,
        empData: any
    ): Observable<EmpBasicDetails> {
        return this.httpClient.post<any>(
            `http://192.168.102.92:8111/employee/saveEmployee/${adminCode}`,
            empData,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    uploadImage(empId: string, adminCode: string, formData: FormData) {
        return this.httpClient.post(
            `http://192.168.102.92:8111/employee/saveEmployeeImage/${empId}/${adminCode}`,
            formData,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    updateEmployeeDetails(adminCode: string, empData: any): Observable<any> {
        return this.httpClient.put<any>(
            `http://192.168.102.92:8111/employee/updateEmployee/${adminCode}`,
            empData,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }
    
    filterDatabyDepartmentId(id: number) {
        return this.httpClient.get(
            `http://192.168.102.92:8111/employee/getAllEmployeeDetailsByDepartmentId/${id}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }
    filterDatabyTeamId(id: number) {
        return this.httpClient.get(
            `http://192.168.102.92:8111/employee/getAllEmployeeDetailsByTeamId/${id}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }
    filterDataByName(name: string) {
        return this.httpClient.get(
            `http://192.168.102.92:8111/employee/getEmployeeDetailsByNameContaining/${name}`,
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    deleteEmployeeDetails(empId: Number, adminCode: String): Observable<any> {
        return this.httpClient.put(
            `http://192.168.102.92:8118/employee/deleteEmployeeDetails/${empId}/${adminCode}`,
            {},
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }

    activateEmployee(empId: Number, adminCode: String): Observable<any> {
        return this.httpClient.put(
            `http://192.168.102.92:8111/employee/activeEmployeeDetails/${empId}/${adminCode}`,
            {},
            {
                headers: new HttpHeaders({ token: this.token }),
            }
        );
    }
}
