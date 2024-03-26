import { TestBed } from '@angular/core/testing';

import { EmployeeServiceService } from './employee-service.service';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('EmployeeServiceService', () => {
    let service: EmployeeServiceService;
    let httpTestingController : HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports:[HttpClientTestingModule]
        });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(EmployeeServiceService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    it('Get All Employee Data', () => {
        let result: any;
        service.fetchEmpsData(1,20).subscribe(t => {
          result = t;
        });
        const req = httpTestingController.expectOne({
          method: "GET",
          url: 'http://192.168.102.92:8111/employee/getAllEmployeeDetails/1/20'
        });
        req.flush([]);
        expect(result != null && result != undefined).toBeTruthy();
    });
    // it('should be created', () => {
    //     expect(service.fetchSingleEmpBasic(74)).toBeTruthy();

    // });
    // it('should be created', () => {
    //     expect(service.filterDatabyDepartmentId(47)).toBeTruthy();
    // });
    // it('should be created', () => {
    //     expect(service.filterDataByName('')).toBeTruthy();
    // });
    // it('should be created', () => {
    //     expect(service).toBeTruthy();
    // });

    

      
});
