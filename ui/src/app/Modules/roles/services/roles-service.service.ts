import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface roleData {
    priviledge: string;
    priviledgeName: string;
}

export const permissions = [
    {
        label: "Employees",
        shortForm: 'E',
        route: false,
        nested: [
            ['View', true, 'V'],
            ['Add', false, 'S'],
            ['Update', false, 'U'],
            ['Delete', false, 'D'],
        ]
    },
    {
        route: false,
        shortForm: 'D',
        label: "Departments / Teams",
        nested: [
            ['View', true, 'V'],
            ['Add', false, 'S'],
            ['Update', false, 'U'],
            ['Delete', false, 'D'],
        ]
    },
    {
        label: "Reports",
        shortForm: 'R',
        route: false,
        nested: [
            ['Download', false, 'W']
        ]
    },
    {
        label: "Activity Logs",
        shortForm: 'L',
        route: false,
        nested: [
            ['View', true, 'V'],
        ]
    }
]

@Injectable({
    providedIn: 'root'
})
export class RolesServiceService {

    token: any;

    constructor(private _httpClient: HttpClient) {
        this.token = sessionStorage.getItem('token');
    }

    getAllRoles(): Observable<any> {
        return this._httpClient.get<any>(`http://192.168.102.92:8111/user/getPriviledgeDetails`, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    addNewRole(newRoleData: roleData): Observable<any> {
        return this._httpClient.post<any>(`http://192.168.102.92:8111/user/savePriviledgeDetails`, newRoleData, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    getSingleRole(roleId: number): Observable<any> {
        return this._httpClient.get<any>(`http://192.168.102.92:8111/user/getPriviledgeDetailsById/${roleId}`, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    updateRoleDetails(roleData: { priviledgeId: number, priviledgeName: string, priviledge: string }): Observable<any> {
        return this._httpClient.put<any>(`http://192.168.102.92:8111/user/updatePriviledgeDetails`, roleData, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    deleteRoleDetails(roleId: string): Observable<any> {
        console.log('api', roleId)
        return this._httpClient.delete<any>(`http://192.168.102.92:8111/user/deletePriviledgeDetails/${roleId}`, {
            headers: new HttpHeaders({ token: this.token })
        });
    }
}
