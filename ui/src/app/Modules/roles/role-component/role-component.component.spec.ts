import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleComponentComponent } from './role-component.component';
import {  HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

describe('RoleComponentComponent', () => {
    let component: RoleComponentComponent;
    let fixture: ComponentFixture<RoleComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoleComponentComponent ],
      imports:[HttpClientModule,MatSnackBarModule,MatTableModule,RouterModule.forRoot([])],
      providers:[HttpClientModule]
    })
    .compileComponents();

        fixture = TestBed.createComponent(RoleComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
