import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DepartmentServiceService } from 'src/app/Modules/departments/services/department-service.service';
import { TeamServiceService } from 'src/app/Modules/departments/services/team-service.service';
import { ErrorComponent } from '../error/error.component';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.scss']
})
export class ConfirmComponent implements OnInit {
  departmentId :number = this.data.departmentId;
  teamId:number = this.data.teamId;
  deleteName :string = "";
  isConfirmReqProcessing:boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any, 
    private _departmentService: DepartmentServiceService,
    private teamService :TeamServiceService ,
    private dialog :MatDialogRef<ConfirmComponent>,
    private router:Router,
    private dialogMat:MatDialog
    ) {
    console.log("ddd", data.userToBeDeleted)
  }
  
  ngOnInit():void{
    if(this.departmentId && this.teamId){
      this.deleteName = "Team"
    }
    else if(this.departmentId){
      this.deleteName = "Department"
    }
  }

  deleteApi() {
    // console.log(this.data.departmentId);
    this.isConfirmReqProcessing = true;
    if(this.data.teamId){
      this.teamService.deleteTeam(this.data.teamId).subscribe({
        next: (response: any) => {
          console.log(response);
        }, error: (error: HttpErrorResponse) => {
          this.isConfirmReqProcessing = false;
          console.log(error);
          if(error.status!=401){
            this.router.navigate(['something-went-wrong']);
        } else{
          this.dialogMat.open(ErrorComponent, {
              panelClass: 'modal',
              
              width: '500px',
              backdropClass: 'backdropBackground',
              disableClose: true,
          });
      }
        }, complete: () => {
          this.isConfirmReqProcessing = false;
          this.router.navigate(["departments/"+ this.departmentId]);
          console.log("Team Deleted!!!");
          this.dialog.close({reload:true})
        }
      })
    }else if (this.data.departmentId ) {
      console.log(this.data);
      
      this._departmentService.deleteDepartment(this.data.departmentId).subscribe({
        next: (response: any) => {
          console.log(response);
        }, error: (error: HttpErrorResponse) => {
          this.isConfirmReqProcessing = false;
          console.log(error);
          if(error.status!=401){
            this.router.navigate(['something-went-wrong']);
        } else{
          this.dialogMat.open(ErrorComponent, {
              panelClass: 'modal',
              
              width: '500px',
              backdropClass: 'backdropBackground',
              disableClose: true,
          });
      }
        }, complete: () => {
          console.log("Department Deleted!!!");
          this.isConfirmReqProcessing = false;
          this.router.navigate(["departments"]);
          this.dialog.close({reload:true})
        }
      })
    }
  }
}
