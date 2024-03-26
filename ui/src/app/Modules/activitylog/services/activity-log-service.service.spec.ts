import { TestBed } from '@angular/core/testing';

import { ActivityLogServiceService } from './activity-log-service.service';
import { HttpClientModule,HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('ActivityLogServiceService', () => {
  let service: ActivityLogServiceService;
  let httpTestingController : HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers:[ActivityLogServiceService]
    });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(ActivityLogServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('Logs Of Particular Employee Are Return', () => {
    let result: any;
    service.getUpdateLogsByEmpCode('emp01').subscribe(t => {
      result = t;
    });
    const req = httpTestingController.expectOne({
      method: "GET",
      url: 'http://192.168.102.92:8118/activityLogs/getDetailsOfUpdateLogByEmpCode/emp01'
    });
    req.flush({});
    expect(result != null && result != undefined).toBeTruthy();
  }); 

  it('Changes Of Particular User/Admin Are Return', () => {
      let result: any;
      service.getUpdateLogsByAdminCode('emp01').subscribe(t => {
        result = t;
      });
      const req = httpTestingController.expectOne({
        method: "GET",
        url: 'http://192.168.102.92:8111/activityLogs/getDetailsOfUpdateLogByAdminCode/emp01'
      });
      req.flush({});
      expect(result != null && result != undefined).toBeTruthy();
  });
  // it('Updated Logs Are Return', () => {
  //   expect(service.getUpdatedLogs).toBeTruthy();
  // });
  // it('Add/Delete Logs Are Return', () => {
  //   expect(service.getAddDeleteLogs).toBeTruthy();
  // });
  // it('Updated Logs With Given Duration Are Return', () => {
  //   expect(service.getDurationLogs('update','2023-04-20','2023-04-22')).toBeTruthy();
  // });
  // it('Add/Delete Logs With Given Duration Are Return', () => {
  //   expect(service.getDurationLogs('insert','2023-04-20','2023-04-22')).toBeTruthy();
  // });
  // it('Logs Of Particular Employee Are Return', () => {
  //   expect(service.getUpdateLogsByEmpCode('emp01')).toBeTruthy();
  // }); 
  // it('Changes Of Particular User/Admin Are Return', () => {
  //   expect(service.getAddDeleteLogsByAdminCode('E02')).toBeTruthy();
  // });
  
});
