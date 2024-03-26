import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DepartmentsComponentComponent } from './departments-component/departments-component.component';
import { SingleDepartmentComponent } from './single-department/single-department.component';
import { TeamComponent } from './single-department/team/team.component';

const routes: Routes = [
    {
        path: '',
        children: [
            {
                path: "",
                component: DepartmentsComponentComponent
            },
            {
                path: ':id',
                children: [
                    {
                        path: "",
                        component: SingleDepartmentComponent
                    },
                    {
                        path: ':team',
                        component: TeamComponent
                    },
                ]
            },
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class DepartmentsRoutingModule { }
