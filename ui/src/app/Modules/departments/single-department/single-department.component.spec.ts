import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleDepartmentComponent } from './single-department.component';
import { RouterModule } from '@angular/router';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import {  MatProgressBarModule } from '@angular/material/progress-bar';

describe('SingleDepartmentComponent', () => {
    let component: SingleDepartmentComponent;
    let fixture: ComponentFixture<SingleDepartmentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [SingleDepartmentComponent],
            imports: [MatProgressSpinnerModule,MatProgressBarModule, HttpClientModule, RouterModule.forRoot([]), MatDialogModule],
        }).compileComponents();

        fixture = TestBed.createComponent(SingleDepartmentComponent);
        component = fixture.componentInstance;

        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
