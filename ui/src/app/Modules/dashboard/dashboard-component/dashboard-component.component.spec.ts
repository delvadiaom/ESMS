import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponentComponent } from './dashboard-component.component';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialogModule } from '@angular/material/dialog';

describe('DashboardComponentComponent', () => {
    let component: DashboardComponentComponent;
    let fixture: ComponentFixture<DashboardComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardComponentComponent ],
      imports:[HttpClientModule,MatProgressBarModule,MatDialogModule],
    })
    .compileComponents();
        fixture = TestBed.createComponent(DashboardComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
