import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsComponentComponent } from './reports-component.component';
import { GraphCardComponent } from '../../shared/components/graph-card/graph-card.component';
import { HttpClientModule } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatDialogModule } from '@angular/material/dialog';

describe('ReportsComponentComponent', () => {
    let component: ReportsComponentComponent;
    let fixture: ComponentFixture<ReportsComponentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ReportsComponentComponent],
            imports:[HttpClientModule,MatTableModule,MatProgressBarModule,MatDialogModule]
        }).compileComponents();

        fixture = TestBed.createComponent(ReportsComponentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
