import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PageNotFoundComponent } from './Modules/shared/components/page-not-found/page-not-found.component';
import { LoginComponent } from './Modules/shared/components/login/login.component';
import { AdminComponent } from './Modules/shared/components/admin/admin.component';
import { LoginGuard } from './Guards/login.guard';
import { AuthGuard } from './Guards/auth.guard';
import { ApiFailedComponent } from './Modules/shared/components/api-failed/api-failed.component';

const routes: Routes = [
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'login', component: LoginComponent ,canActivate:[AuthGuard]},
    {
        path: '', canActivate:[LoginGuard], component: AdminComponent, children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
            { path: 'dashboard', loadChildren: () => import("./Modules/dashboard/dashboard.module").then(res => res.DashboardModule) },
            { path: 'employees', loadChildren: () => import("./Modules/employee/employee.module").then(res => res.EmployeeModule) },
            { path: 'departments', loadChildren: () => import("./Modules/departments/departments.module").then(res => res.DepartmentsModule) },
            { path: 'users', loadChildren: () => import("./Modules/users/users.module").then(res => res.UsersModule) },
            { path: 'roles', loadChildren: () => import("./Modules/roles/roles.module").then(res => res.RolesModule) },
            { path: 'reports', loadChildren: () => import("./Modules/reports/reports.module").then(res => res.ReportsModule) },
            { path: 'activitylogs', loadChildren: () => import("./Modules/activitylog/activitylog.module").then(res => res.ActivitylogModule) },
            {path:'something-went-wrong',component:ApiFailedComponent},
            { path: "**", component: PageNotFoundComponent }

        ]
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}