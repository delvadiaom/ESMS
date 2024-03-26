import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponentComponent } from './dashboard-component/dashboard-component.component';
import { SharedModule } from '../shared/shared.module';
import { HttpClientModule } from '@angular/common/http';
import { DashboardServiceService } from './services/dashboard-service.service';
import { ChartService } from '../shared/Services/chart-service.service';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatProgressBarModule } from '@angular/material/progress-bar';

@NgModule({
    declarations: [DashboardComponentComponent],
    imports: [
        CommonModule,
        DashboardRoutingModule,
        SharedModule,
        HttpClientModule,
        MatProgressSpinnerModule,
        MatProgressBarModule,
        SharedModule
    ],
    providers: [DashboardServiceService, ChartService],
})
export class DashboardModule {}
