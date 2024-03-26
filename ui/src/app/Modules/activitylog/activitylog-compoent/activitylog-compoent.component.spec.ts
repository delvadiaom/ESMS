import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitylogCompoentComponent } from './activitylog-compoent.component';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('ActivitylogCompoentComponent', () => {
    let component: ActivitylogCompoentComponent;
    let fixture: ComponentFixture<ActivitylogCompoentComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ActivitylogCompoentComponent],
            imports:[MatDialogModule,MatPaginatorModule,MatSnackBarModule,MatTableModule,BrowserAnimationsModule,HttpClientModule]
        })
            .compileComponents();

        fixture = TestBed.createComponent(ActivitylogCompoentComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        // expect(component).toBeTruthy();
    });
});
