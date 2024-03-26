import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedRoutingModule } from './shared-routing.module';
import { BodyComponent } from './components/body/body.component';
import { MainCardComponent } from './components/main-card/main-card.component';
import { GraphCardComponent } from './components/graph-card/graph-card.component';
import { SidenavComponent } from './components/sidenav/sidenav.component';
import { TopnavComponent } from './components/topnav/topnav.component';
import { HighchartsChartModule } from 'highcharts-angular';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatButtonModule } from '@angular/material/button';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { LoginComponent } from './components/login/login.component';
import { AdminComponent } from './components/admin/admin.component';
import { CurrencyConverterPipe } from 'src/app/pipes/currency-converter.pipe';
import { ChartService } from './Services/chart-service.service';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { HttpClientModule } from '@angular/common/http';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { ConfirmComponent } from './components/confirm/confirm.component';
import { ErrorComponent } from './components/error/error.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ApiFailedComponent } from './components/api-failed/api-failed.component';

@NgModule({
    declarations: [
        BodyComponent,
        MainCardComponent,
        GraphCardComponent,
        SidenavComponent,
        TopnavComponent,
        EditProfileComponent,
        PageNotFoundComponent,
        LoginComponent,
        AdminComponent,
        CurrencyConverterPipe,
        ConfirmComponent,
        ErrorComponent,
        ApiFailedComponent,
        // TablesComponent,
    ],
    imports: [
        CommonModule,
        HighchartsChartModule,
        SharedRoutingModule,
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
        MatExpansionModule,
        MatTooltipModule,
        MatDialogModule,
        MatButtonModule,
        MatSnackBarModule,
        MatProgressSpinnerModule,
    ],
    exports: [
        BodyComponent,
        MainCardComponent,
        GraphCardComponent,
        SidenavComponent,
        TopnavComponent,
        EditProfileComponent,
        PageNotFoundComponent,
        LoginComponent,
        AdminComponent,
        ApiFailedComponent
    ],
    providers: [ChartService],
})
export class SharedModule {}
