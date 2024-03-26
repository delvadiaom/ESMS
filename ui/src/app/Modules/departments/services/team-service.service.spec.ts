import { TestBed } from '@angular/core/testing';

import { TeamServiceService } from './team-service.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';


describe('TeamServiceService', () => {
  let service: TeamServiceService;
  let httpTestingController : HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(TeamServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Logs Of Particular Employee Are Return', () => {
    let result: any;
    service.getSingleTeamData(75).subscribe(t => {
      result = t;
    });
    const req = httpTestingController.expectOne({
      method: "GET",
      url: 'http://192.168.102.92:8111/team/getTeamById/75'
    });
    req.flush({});
    expect(result != null && result != undefined).toBeTruthy();
  });
  it('Logs Of Particular Employee Are Return', () => {
    let result: any;
    service.fetchEmpsDataWithTeamId(75).subscribe(t => {
      result = t;
    });
    const req = httpTestingController.expectOne({
      method: "GET",
      url: 'http://192.168.102.92:8111/employee/getAllEmployeeDetailsByTeamId/75'
    });
    req.flush({});
    expect(result != null && result != undefined).toBeTruthy();
  });
});
