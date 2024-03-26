import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DepartmentsComponentComponent } from './departments-component/departments-component.component';
import { AddDepartmentComponent } from './add-department/add-department.component';
import { SingleDepartmentComponent } from './single-department/single-department.component';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
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
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'
import { SharedModule } from '../shared/shared.module';
import { DepartmentsRoutingModule } from './departments-routing.module';
import { TeamComponent } from './single-department/team/team.component';
import { AddTeamComponent } from './single-department/team/add-team/add-team.component';
import { DepartmentServiceService } from './services/department-service.service';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    declarations: [
        DepartmentsComponentComponent,
        AddDepartmentComponent,
        SingleDepartmentComponent,
        TeamComponent,
        AddTeamComponent
    ],
    imports: [
        CommonModule,
        SharedModule,
        ReactiveFormsModule,
        DepartmentsRoutingModule,
        MatProgressSpinnerModule,
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
        HttpClientModule,
        SharedModule
    ],
    providers: [
        DepartmentServiceService
    ]
})
export class DepartmentsModule { }
