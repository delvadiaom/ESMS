import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEmployeeComponent } from './add-employee.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';

describe('AddEmployeeComponent', () => {
    let component: AddEmployeeComponent;
    let fixture: ComponentFixture<AddEmployeeComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [AddEmployeeComponent],
            imports:[HttpClientModule,MatProgressSpinnerModule,ReactiveFormsModule,MatDialogModule,MatSnackBarModule,RouterModule.forRoot([])]
        })
            .compileComponents();

        fixture = TestBed.createComponent(AddEmployeeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
