import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { UsersComponentComponent } from './users-component/users-component.component';
import { AddUserComponent } from './add-user/add-user.component';
// import { ViewUserComponent } from './view-user/view-user.component';

import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import {MatButtonToggleGroup, MatButtonToggleModule} from '@angular/material/button-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatExpansionModule } from '@angular/material/expansion';
import { SharedModule } from '../shared/shared.module';
import { DeleteUserComponent } from './delete-user/delete-user.component';
import { EditUserComponent } from './edit-user/edit-user.component';
import { HttpClientModule } from '@angular/common/http';
import { GetNamePipe } from './pipe/get-name.pipe';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';



@NgModule({
    declarations: [
        UsersComponentComponent,
        AddUserComponent,
        // ViewUserComponent,
    DeleteUserComponent,
    EditUserComponent,
    GetNamePipe,
    ],
    imports: [
        CommonModule,
        UsersRoutingModule,
        ReactiveFormsModule,
        FormsModule,

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
        MatButtonToggleModule,
        HttpClientModule,
        MatProgressSpinnerModule
        
    ]
})
export class UsersModule { }
