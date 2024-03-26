import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RolesRoutingModule } from './roles-routing.module';
import { RoleComponentComponent } from './role-component/role-component.component';

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
import { AddRoleComponent } from './add-role/add-role.component';
import { MatProgressSpinner, MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EditRoleComponent } from './edit-role/edit-role.component';


@NgModule({
    declarations: [
        RoleComponentComponent,
        AddRoleComponent,
        EditRoleComponent
    ],
    imports: [
        CommonModule,
        RolesRoutingModule,
        ReactiveFormsModule,

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
        MatProgressSpinnerModule
    ]
})
export class RolesModule { }
