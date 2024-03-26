import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeComponentComponent } from './employee-component/employee-component.component';
import { AddEmployeeComponent } from './add-employee/add-employee.component';
import { SingleEmployeeComponent } from './single-employee/single-employee.component';

const routes: Routes = [
    { path: '', component: EmployeeComponentComponent },
    { path: 'add-employee', component: AddEmployeeComponent },
    { path: ':id', component: SingleEmployeeComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class EmployeeRoutingModule { }
