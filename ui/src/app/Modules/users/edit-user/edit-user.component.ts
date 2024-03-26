import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Validators } from '@angular/forms';

import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import {
    animate,
    keyframes,
    style,
    transition,
    trigger,
} from '@angular/animations';
import { UsersService } from '../services/users.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { editUserData } from '../Model/UserData.model';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';

@Component({
    selector: 'app-edit-user',
    templateUrl: './edit-user.component.html',
    styleUrls: ['./edit-user.component.scss'],
    animations: [
        trigger('rotate', [
            transition(':enter', [
                animate(
                    '600ms',
                    keyframes([
                        style({ transform: 'rotate(0deg)', offset: '0' }),
                        style({ transform: 'rotate(180deg)', offset: '1' }),
                    ])
                ),
            ]),
        ]),
    ],
})
export class EditUserComponent implements OnInit {
    editUserForm: FormGroup;

    roles: any;
    isFetched: boolean = false;
    editBtnText: string = 'Update';
    btnEnable: boolean = false;

    constructor(
        @Inject(MAT_DIALOG_DATA) public userData: any,
        private userservice: UsersService,
        private dialog: MatDialogRef<EditUserComponent>,
        private dialogMat:MatDialog,
        private router:Router,
    ) {
        this.fetchRoles();

        // Edit Form
        this.editUserForm = new FormGroup({
            empName: new FormControl('', [Validators.required]),
            empEmail: new FormControl('', [
                Validators.required,
                Validators.email,
            ]),
            empRole: new FormControl([Validators.required]),
        });

        // Patching values
        this.editUserForm.patchValue({
            empName: (
                this.userData.data.email.charAt(0).toUpperCase() +
                this.userData.data.email.slice(1)
            ).split('@')[0],
            empEmail: this.userData.data.email,
            empRole: this.userData.data.priviledgeId,
        });
    }

    ngOnInit(): void {}

    // Fetching all roles
    fetchRoles() {
        this.userservice.getAllRols().subscribe({
            next: (data) => {
                this.roles = data;
            },
            error: (error:HttpErrorResponse) => {
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
                    let temp = {
                        key: role.priviledgeId,
                        name: role.priviledgeName,
                    };
                    allRoles.push(temp);
                }
                this.roles = allRoles;
                this.isFetched = true;
            },
        });
    }

    // Edit user
    editUser() {
        this.editBtnText = 'Updating...';
        this.btnEnable = true;
        let editUser: editUserData = {
            email: this.editUserForm.controls['empEmail'].value,
            password: '',
            priviledgeId: parseInt(this.editUserForm.controls['empRole'].value),
        };

        console.log(editUser);
        this.userservice.editUser(editUser).subscribe({
            next: (data: HttpResponse<any>) => {
                console.log(data);
            },
            error: (error: HttpErrorResponse) => {
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
                this.dialog.close({ reload: true });
            },
            complete: () => {
                console.log('Done');
                this.dialog.close({ reload: true });
            },
        });
    }
}
