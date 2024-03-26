import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RoleComponentComponent } from './role-component/role-component.component';
import { AddRoleComponent } from './add-role/add-role.component';
import { EditRoleComponent } from './edit-role/edit-role.component';

const routes: Routes = [
    { path: '', component: RoleComponentComponent },
    { path: 'add-role', component: AddRoleComponent },
    { path: 'edit/:roleid', component: EditRoleComponent },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RolesRoutingModule { }
