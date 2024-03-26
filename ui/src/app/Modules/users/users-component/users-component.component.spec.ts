import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersComponentComponent } from './users-component.component';
import { HttpClientModule } from '@angular/common/http';
import {  MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { FormsModule } from '@angular/forms';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

describe('UsersComponentComponent', () => {
    let component: UsersComponentComponent;
    let fixture: ComponentFixture<UsersComponentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [UsersComponentComponent],
            imports:[HttpClientModule,MatDialogModule,MatTableModule,MatTooltipModule,Ng2SearchPipeModule,FormsModule,MatProgressSpinnerModule]
        })
            .compileComponents();

        fixture = TestBed.createComponent(UsersComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
