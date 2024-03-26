import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmployeeRoutingModule } from './employee-routing.module';
import { EmployeeComponentComponent } from './employee-component/employee-component.component';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { AddEmployeeComponent } from './add-employee/add-employee.component';
import { SingleEmployeeComponent } from './single-employee/single-employee.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatExpansionModule } from '@angular/material/expansion';
import { HttpClientModule } from '@angular/common/http';
import { EmployeeServiceService } from './services/employee-service.service';
import { ActivitylogModule } from '../activitylog/activitylog.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@NgModule({
    declarations: [
        EmployeeComponentComponent,
        AddEmployeeComponent,
        SingleEmployeeComponent,
        
    ],
    imports: [
        CommonModule,
        EmployeeRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,

        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatIconModule,
        MatButtonModule,
        Ng2SearchPipeModule,
        MatSlideToggleModule,
        MatCardModule,
        MatDividerModule,
        MatProgressBarModule,
        MatAutocompleteModule,
        MatDialogModule,
        MatTooltipModule,
        MatExpansionModule,
        ActivitylogModule,
        MatProgressSpinnerModule
    ],
    providers: [EmployeeServiceService]
})
export class EmployeeModule { }
