import { TestBed } from '@angular/core/testing';

import { ReportServiceService } from './report-service.service';
import {
    HttpClientTestingModule,
} from '@angular/common/http/testing';

describe('ReportServiceService', () => {
    let service: ReportServiceService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [ReportServiceService],
        });
        service = TestBed.inject(ReportServiceService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    it('Return All Department Report', () => {
        expect(service.getAllDepartmentReport()).toBeTruthy();
    });
    it('Return Departmnet Wise Costing', () => {
        expect(service.getDepartmnetWiseCosting()).toBeTruthy();
    });
    it('Return Experience Wise Salary Chart', () => {
        expect(service.getExperienceWiseSalaryChart()).toBeTruthy();
    });
    it('Return Individual Department Employee List', () => {
        expect(service.getIndividualDeparment(1, 20)).toBeTruthy();
    });
    it('Return Team Wise CostChart', () => {
        expect(service.getTeamWiseCostChart('47')).toBeTruthy();
    });
});
