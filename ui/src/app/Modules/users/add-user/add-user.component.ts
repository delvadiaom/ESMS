import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, ElementRef, Inject, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button'
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { UsersService } from '../services/users.service';
import { error } from 'highcharts';
import { Observable, Subscription } from 'rxjs';
import { UserDataInterface } from '../users-component/users-component.component';
import { passwordMatch } from '../Validators/passwordMatch';
import { HttpErrorResponse } from '@angular/common/http';
import { newUser } from '../Model/UserData.model';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';


@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss'],
  animations: [
    trigger('rotate', [
      transition(':enter', [
        animate('600ms',
          keyframes([
            style({
              transform:
                'rotate(0deg)', offset: '0'
            }),
            style({ transform: 'rotate(180deg)', offset: '1' })
          ])
        )
      ])
    ]),
  ],
  providers: [UsersService]
})
export class AddUserComponent {
  firstBtnText: string = 'Fetch';
  addBtnText: string = "Save";
  btnEnable:boolean=true;
  isValidEmployee: boolean = false;
  isFetchFail: boolean = false;
  firstForm: FormGroup;
  secondForm: FormGroup;
  addUserSubscriber: Subscription | undefined;
  roles: any;
  private empCode!: string;



  constructor(@Inject(MAT_DIALOG_DATA) public data: { title: string },private router:Router, private dialogMat:MatDialog, private userDataService: UsersService, private dialog: MatDialogRef<AddUserComponent>) {
    // Fetching roles
    this.fetchRoles();

    // First Form Group (Email Id)
    this.firstForm = new FormGroup({
      empEmail: new FormControl("", {
        validators: [Validators.required, Validators.email],
      })
    });

    // Second Form Group (Role & Password)
    this.secondForm = new FormGroup({
      empName: new FormControl('', [Validators.required]),
      empEmail: new FormControl('', [Validators.required, Validators.email]),
      empRole: new FormControl('', [Validators.required]),
      empPassword: new FormControl('', [Validators.required, Validators.minLength(8), Validators.pattern(/^\S*$/), Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')]),
      confirmPassword: new FormControl('', [Validators.required]),
    }, { validators: passwordMatch });

  }


  fetchRoles() {
    this.userDataService.getAllRols().subscribe({
      next: (data) => { this.roles = data },
      error: (error:HttpErrorResponse) => { console.log(error)
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
      },
      complete: () => {
        let allRoles = [];
        for (let role of this.roles) {
          let temp = { key: role.priviledgeId, name: role.priviledgeName };
          allRoles.push(temp)
        }
        this.roles = allRoles;
      }
    });

  }


  // Called on Fetch Button Click
  // Fetch Employee Data
  fetchData() {

    let fetchedUser: any;
    this.firstBtnText = "Fetching...";
    this.btnEnable=false;

    this.userDataService.fetchDataFromEmail(this.firstForm.controls['empEmail'].value).subscribe(
      {
        next: (data) => {
          fetchedUser = data;
          this.isValidEmployee = true;

          this.secondForm.patchValue({
            empName: fetchedUser.fullName,
            empEmail: fetchedUser.emailId,
          });
          this.empCode = fetchedUser.empCode;
        }, error: (error:HttpErrorResponse) => {
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
         
          this.dialog.close();


        }, complete: () => {
          console.log("User Fetched Successfully!")
        }

      }
    );

    this.firstForm.reset();
    return;
  }

  // Called on Add User Click
  // Adds User
  addUser() {
    this.addBtnText = "Saving...";
    this.btnEnable=true;

    let newUser: newUser = {
      "email": this.secondForm.controls['empEmail'].value,
      "priviledgeId": Number(this.secondForm.controls['empRole'].value),
      "password": this.secondForm.controls['empPassword'].value,
      "empCode": this.empCode
    }
    let Observable = this.userDataService.addUser(newUser)
    this.addUserSubscriber = Observable.subscribe({
      next: (result: any) => {
        
      }, error: (error: HttpErrorResponse) => {
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
        console.log("error");
        
      }, complete: () => {
        console.log("User added successfully!");
        this.dialog.close({ reload: true })
      }
    })

  }

  ngOnDestroy() {
    this.addUserSubscriber?.unsubscribe();
  }

  // Navigate Back
  back() {
    this.isValidEmployee = false;
    this.dialog.close();
  }


}
