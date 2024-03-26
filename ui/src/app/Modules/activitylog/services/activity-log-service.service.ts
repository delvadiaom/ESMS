import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface UpdateLog {
    adminCode: string;
    adminName: string;
    columnName: string;
    empCode: string;
    empName: string;
    logId: number;
    oldValue: string;
    newValue: string;
    timeStamp: Date;
}

export interface AddDeleteLog {
    adminCode: string;
    adminName: string;  
    empCode: string;
    empName: string;
    logId: number;
    timeStamp: Date;
}

@Injectable({
  providedIn: 'root'
})

export class ActivityLogServiceService {
     
    token1:any = sessionStorage.getItem('token');

    constructor(private _http: HttpClient) { }

    getUpdatedLogs(pgNo:number): Observable<UpdateLog[]> {
        return this._http.post<UpdateLog[]>(
            'http://192.168.102.92:8111/activityLogs/getDetailsOfLog',
            { action: 'update' ,page:pgNo},
            {
                headers: { token: this.token1 },
            }
        );
    }

    getAddDeleteLogs(pgNo: number): Observable<AddDeleteLog[]> {
        console.log(pgNo);
        
        return this._http.post<AddDeleteLog[]>(
            'http://192.168.102.92:8111/activityLogs/getDetailsOfLog',
            { action: 'insert' ,page:pgNo},
            {
                headers: { token: this.token1 },
            }
        );
    }

    getDurationLogs(log: string, start: string, end: string): Observable<any> {
        return this._http.post<any>(
            'http://192.168.102.92:8111/activityLogs/getDetailsOfLog',  
            { action: log,page:1, start_date: start, end_date: end },
            {
                headers: { token: this.token1 },
            }
        );
    }

    getUpdateLogsByAdminCode(id: string): Observable<UpdateLog[]> {
        return this._http.get<UpdateLog[]>(
            `http://192.168.102.92:8111/activityLogs/getDetailsOfUpdateLogByAdminCode/${id}`,
            {
                headers: { token: this.token1 },
            }
        );
    }
 

    getUpdateLogsCount(): Observable<number> {
        return this._http.get<number>(
            'http://192.168.102.92:8111/activityLogs/updateLogCount',
            {
                headers: { token: this.token1 },
            }
        );
    }

    getAddDeleteLogsCount(): Observable<number> {
        return this._http.get<number>(
            'http://192.168.102.92:8118/activityLogs/insertDeleteLogCount',
            {
                headers: { token: this.token1 },
            }
        );
    }

    getUpdateLogsByEmpCode(code: string): Observable<UpdateLog[]> {
        return this._http.get<UpdateLog[]>(
            `http://192.168.102.92:8118/activityLogs/getDetailsOfUpdateLogByEmpCode/${code}`,
            {
                headers: { token: this.token1 },
            }
        );
    }
    // Not Used
    getAddDeleteLogsByEmpCode(code: string): Observable<AddDeleteLog[]> { 
        return this._http.get<AddDeleteLog[]>(
            `http://192.168.102.92:8118/activityLogs/getDetailsOfInsertDeleteLogByEmpCode/${code}`,
            {
                headers: { token: this.token1 },
            }
        );
    }
     getAddDeleteLogsByAdminCode(id: string): Observable<AddDeleteLog[]> {
        return this._http.get<AddDeleteLog[]>(
            `http://192.168.102.92:8111/activityLogs/getDetailsOfInsertDeleteLogByAdminCode/${id}`,
            {
                headers: { token: this.token1 },
            }
        );
    }
}
