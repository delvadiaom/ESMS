import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoleComponent } from './add-role.component';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { ReactiveFormsModule } from '@angular/forms';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RouterModule } from '@angular/router';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

describe('AddRoleComponent', () => {
    let component: AddRoleComponent;
    let fixture: ComponentFixture<AddRoleComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [AddRoleComponent],
            imports:[RouterModule.forRoot([]), HttpClientModule,MatCardModule,MatDividerModule,ReactiveFormsModule,MatProgressBarModule,MatSlideToggleModule]
        })
            .compileComponents();

        fixture = TestBed.createComponent(AddRoleComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
