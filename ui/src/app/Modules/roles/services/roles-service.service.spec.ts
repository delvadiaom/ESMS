import { TestBed } from '@angular/core/testing';

import { RolesServiceService } from './roles-service.service';
import {  HttpClientModule } from '@angular/common/http';
import {HttpClientTestingModule} from '@angular/common/http/testing';


describe('RolesServiceService', () => {
    let service: RolesServiceService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports:[HttpClientTestingModule]
        });
        service = TestBed.inject(RolesServiceService);
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });
    it('Get All Roles Data',()=>{
        expect(service.getAllRoles).toBeTruthy();
    });
    it('Get Single Role Data',()=>{
        expect(service.getSingleRole(4)).toBeTruthy();
    })


});
