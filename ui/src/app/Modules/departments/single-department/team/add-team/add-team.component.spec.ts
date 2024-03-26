import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTeamComponent } from './add-team.component';
import { MatDialogModule, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AddTeamComponent', () => {
    let component: AddTeamComponent;
    let fixture: ComponentFixture<AddTeamComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [AddTeamComponent],
            imports:[MatDialogModule,HttpClientModule,ReactiveFormsModule,BrowserAnimationsModule],
            providers:[
                { provide: MAT_DIALOG_DATA, useValue: {} },
                { provide: MatDialogRef, useValue: {} }
            ]
        })
            .compileComponents();

        fixture = TestBed.createComponent(AddTeamComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
