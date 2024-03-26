import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentsComponentComponent } from './departments-component.component';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressBarModule } from '@angular/material/progress-bar';

describe('DepartmentsComponentComponent', () => {
    let component: DepartmentsComponentComponent;
    let fixture: ComponentFixture<DepartmentsComponentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [DepartmentsComponentComponent],
            imports: [MatDialogModule,MatProgressBarModule,HttpClientModule],
        })
            .compileComponents();

        fixture = TestBed.createComponent(DepartmentsComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

});
