import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeComponentComponent } from './employee-component.component';
import { HttpClientModule } from '@angular/common/http';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('EmployeeComponentComponent', () => {
    let component: EmployeeComponentComponent;
    let fixture: ComponentFixture<EmployeeComponentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [EmployeeComponentComponent],
            imports:[HttpClientModule,MatDialogModule,MatPaginatorModule,MatTableModule,BrowserAnimationsModule,MatSnackBarModule]
            
        })
            .compileComponents();

        fixture = TestBed.createComponent(EmployeeComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
