import { TestBed } from '@angular/core/testing';

import { DashboardServiceService } from './dashboard-service.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClientModule } from '@angular/common/http';

describe('DashboardServiceService', () => {
    let service: DashboardServiceService;
    let httpTestingController : HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
        });
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.inject(DashboardServiceService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    it('Get DashBoard Data', () => {
        let result: any;
        service.getDashboardData().subscribe(t => {
          result = t;
        });
        const req = httpTestingController.expectOne({
          method: "GET",
          url: 'http://192.168.102.92:8111/dashboard/'
        });
        req.flush({});
        expect(result != null && result != undefined).toBeTruthy();
    });
});
