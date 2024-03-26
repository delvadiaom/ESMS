import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmpData } from '../single-department/team/team.component';


export interface Team {
    teamId: number,
    teamName: string,
    teamCode: string,
    departmentId: number,
    leadBy: string
}

@Injectable({
    providedIn: 'root'
})
export class TeamServiceService {
    token: any = sessionStorage.getItem('token');

    constructor(private _httpClient: HttpClient) { }

    addTeam(teamData: any): Observable<any> {
        console.log("api", teamData);
        return this._httpClient.post<any>("http://192.168.102.92:8111/team/saveTeamDetails", teamData, {
            headers: new HttpHeaders({ token: this.token })
        });
    }


    getSingleTeamData(id: number): Observable<Team> {
        return this._httpClient.get<Team>(`http://192.168.102.92:8111/team/getTeamById/${id}`, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    updateTeam(teamData: any): Observable<any> {
        console.log("api", teamData);
        return this._httpClient.put<any>("http://192.168.102.92:8111/team/updateTeamDetails", teamData, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    fetchEmpsDataWithTeamId(teamId: number): Observable<EmpData[]> {
        return this._httpClient.get<EmpData[]>(`http://192.168.102.92:8111/employee/getAllEmployeeDetailsByTeamId/${teamId}`, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

    deleteTeam(teamId: number): Observable<any> {
        console.log("api", teamId);

        return this._httpClient.put<any>(`http://192.168.102.92:8111/team/deleteTeamDetails/${teamId}`, {}, {
            headers: new HttpHeaders({ token: this.token })
        });
    }

}
