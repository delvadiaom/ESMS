import { AfterViewInit, Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {
    MatDialog,
    MatDialogRef,
    MAT_DIALOG_DATA,
} from '@angular/material/dialog';
import {
    animate,
    keyframes,
    style,
    transition,
    trigger,
} from '@angular/animations';
import {
    DepartmentServiceService,
} from '../services/department-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { addDepartmentResponse } from '../../shared/interfaces/interfaces';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';

@Component({
    selector: 'app-add-department',
    templateUrl: './add-department.component.html',
    styleUrls: ['./add-department.component.scss'],
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



export class AddDepartmentComponent implements OnInit {

    formLoading = false;
    isDeptReqProcessing = false;
    responseObj!: addDepartmentResponse
    addDepartmentForm!: FormGroup;

    constructor(
        @Inject(MAT_DIALOG_DATA)
        public data: { title: string; edit: boolean; departmentId: any },
        private _departmentSevice: DepartmentServiceService,
        private dialog: MatDialogRef<AddDepartmentComponent>,
        private router:Router,
        private dialogMat :MatDialog
    ) { }


    ngOnInit(): void {
        this.loadData();
    }

    loadData() {

        // Loading Started
        this.formLoading = true;


        if (this.data.edit) {  // For Edit Department Form

            // Fetch Data and populate in form
            this._departmentSevice
                .getSingleDeptData(this.data.departmentId)
                .subscribe({


                    next:(response)=>{
                        this.addDepartmentForm = new FormGroup({
                            departmentCode: new FormControl(
                                response.departmentCode,
                                [Validators.required, Validators.maxLength(10), Validators.pattern(/^[a-zA-Z0-9_-]*$/)]
                            ),
                            departmentName: new FormControl(
                                response.departmentName,
                                [Validators.required, Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]
                            ),
                            headedBy: new FormControl(
                                response.headedBy,
                                [Validators.required, Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9_-]*$/)]
                            ),
                            skills: new FormControl(
                                response.skills,
                                Validators.required
                            ),
                        });

                    },
                    error:(err:HttpErrorResponse)=>{
                        if(err.status !=401){
                            this.router.navigate(['something-went-wrong']);
                        }
                        else{
                            this.dialogMat.open(ErrorComponent, {
                                panelClass: 'modal',
                                
                                width: '500px',
                                backdropClass: 'backdropBackground',
                                disableClose: true,
                            });
                        }
                    },
                    complete:()=>{
                        // Loading Ended
                        this.formLoading = false;

                    }
                    

                    
                    
                });

        } else {  // For Add Department Form

            this.addDepartmentForm = new FormGroup({
                departmentCode: new FormControl('', [Validators.required, Validators.maxLength(10), Validators.pattern(/^[a-zA-Z0-9_-]*$/)]),
                departmentName: new FormControl('', [Validators.required, Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]),
                headedBy: new FormControl('', [ Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9_-]*$/)]),
                skills: new FormControl('', Validators.required),
            });

            // Loading Ended
            this.formLoading = false;
        }
    }

    // Submit Data
    submit_department() {

        this.isDeptReqProcessing = true;
        // console.log(this.addDepartmentForm);

        // For Add Department Form
        if (!this.data.edit) {

            const departmentData = this.addDepartmentForm.value;

            this._departmentSevice.addDepartment(departmentData).subscribe({

                next: (response) => {
                    this.responseObj = response;
                    console.log(this.responseObj);
                },
                error: (error: HttpErrorResponse) => {
                    this.isDeptReqProcessing = false;
                    console.log(error);
                    if(error.status !=401){
                        this.router.navigate(['something-went-wrong']);
                    }else{
                        this.dialogMat.open(ErrorComponent, {
                            panelClass: 'modal',
                            
                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                },
                complete: () => {
                    this.isDeptReqProcessing = false;
                    this.dialog.close({ reload: true });
                    console.log('Department Added!!!');
                },
            });

        } else {
            // Update Department
            this.addDepartmentForm.controls['departmentCode'].setValue(this.addDepartmentForm.controls['departmentCode'].value.trim());
            const departmentData = this.addDepartmentForm.value;
            this._departmentSevice
                .updateDepartment({
                    departmentId: this.data.departmentId,
                    ...departmentData,
                })
                .subscribe({
                    next: (response: any) => {
                        this.responseObj = response;
                        console.log(this.responseObj);
                    },
                    error: (error: HttpErrorResponse) => {
                        this.isDeptReqProcessing = false;
                        console.log(error);
                        if(error.status !=401){
                            this.router.navigate(['something-went-wrong']);
                        }else{
                            this.dialogMat.open(ErrorComponent, {
                                panelClass: 'modal',
                                
                                width: '500px',
                                backdropClass: 'backdropBackground',
                                disableClose: true,
                            });
                        }
                    },
                    complete: () => {
                        this.isDeptReqProcessing = false;
                        this.dialog.close({ reload: true });
                        console.log('Department Updated!!!');
                    },
                });
        }
    }
}
