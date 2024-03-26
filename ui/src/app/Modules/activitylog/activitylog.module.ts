import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ActivitylogRoutingModule } from './activitylog-routing.module';
import { ActivitylogCompoentComponent } from './activitylog-compoent/activitylog-compoent.component';
import { SingleUserActivityLogComponent } from './activitylog-compoent/single-user-activity-log/single-user-activity-log.component';
import { ActivityLogServiceService } from './services/activity-log-service.service';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatNativeDateModule } from '@angular/material/core';
import { MatTooltipModule } from '@angular/material/tooltip';

import { HttpClientModule } from '@angular/common/http';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@NgModule({
    declarations: [
        ActivitylogCompoentComponent,
        SingleUserActivityLogComponent
    ],
    imports: [
        CommonModule,
        ActivitylogRoutingModule,
        MatPaginatorModule,
        MatTableModule,
        MatDialogModule,
        MatSortModule,
        MatNativeDateModule,
        FormsModule,
        MatProgressSpinnerModule,
        MatFormFieldModule,
        ReactiveFormsModule,
        MatTooltipModule,
        MatProgressBarModule,
        HttpClientModule
        
    ],
    exports:[SingleUserActivityLogComponent],
    providers:[ActivityLogServiceService]
})
export class ActivitylogModule { }