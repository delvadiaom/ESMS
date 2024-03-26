import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ActivitylogCompoentComponent } from './activitylog-compoent/activitylog-compoent.component';
import { SingleUserActivityLogComponent } from './activitylog-compoent/single-user-activity-log/single-user-activity-log.component';

const routes: Routes = [
  { path: '', component: ActivitylogCompoentComponent },
  {path:'single',component:SingleUserActivityLogComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ActivitylogRoutingModule {

}