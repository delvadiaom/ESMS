import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {


  token: any;
  constructor(private httpClient: HttpClient) {
    this.token = sessionStorage.getItem('token');
  }

  fetchUserData(email: string) {
    return this.httpClient.get<any>(`http://192.168.102.92:8111/employee/getEmployeeDetailsFromEmail/${email}`, {
      headers: new HttpHeaders({ token: this.token })
    })
  }

  getImg(empId:number){
    return this.httpClient.get(`http://192.168.102.92:8118/employee/getEmployeeImage/${empId}`,{
      headers: new HttpHeaders({ token: this.token }),
      responseType:"blob",
    })
  }
  

  setPassword(mail: string, current: string, password: string) {
    return this.httpClient.post<any>('http://192.168.102.92:8111/user/changePassword', {
      mail, current, password
    }, {
      headers: new HttpHeaders({ token: this.token })
    })
  }
}
