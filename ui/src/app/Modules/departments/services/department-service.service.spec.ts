import { TestBed } from '@angular/core/testing';

import { DepartmentServiceService } from './department-service.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('DepartmentServiceService', () => {
    let service: DepartmentServiceService;
    let httpTestingController : HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports:[HttpClientTestingModule]
        });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(DepartmentServiceService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    it('Get Single Department Data', () => {
        let result: any;
        service.getSingleDeptData(47).subscribe(t => {
          result = t;
        });
        const req = httpTestingController.expectOne({
          method: "GET",
          url: 'http://192.168.102.92:8111/department/getDepartmentDetails/47'
        });
        req.flush({});
        expect(result != null && result != undefined).toBeTruthy();
    });

    /* http://192.168.102.92:8111/department/getDepartmentDetails/ */
    it('Get All Department Data', () => {
        let result: any;
        service.getDepartmentData().subscribe(t => {
          result = t;
        });
        const req = httpTestingController.expectOne({
          method: "GET",
          url: 'http://192.168.102.92:8111/department/getAllDepartmentsList'
        });
        req.flush({});
        expect(result != null && result != undefined).toBeTruthy();
    });
    /* http://192.168.102.92:8111/department/getAllDepartmentsList */
});
