import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReportsRoutingModule } from './reports-routing.module';
import { ReportsComponentComponent } from './reports-component/reports-component.component';
import { MatTableModule } from '@angular/material/table';
import { SharedModule } from "../shared/shared.module";
import { ReportServiceService } from './services/report-service.service';
import { HttpClientModule } from '@angular/common/http';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSortModule } from '@angular/material/sort';

@NgModule({
    declarations: [
        ReportsComponentComponent
    ],
    imports: [
        CommonModule,
        ReportsRoutingModule,
        MatTableModule,
        SharedModule,
        HttpClientModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatTooltipModule,
        MatSortModule,
        SharedModule
    ],
    providers:[ReportServiceService]
})
export class ReportsModule { }
