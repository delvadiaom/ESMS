import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DepartmentServiceService } from '../../departments/services/department-service.service';
import { Subscription } from 'rxjs';
import { EmployeeServiceService } from '../services/employee-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-add-employee',
    templateUrl: './add-employee.component.html',
    styleUrls: ['./add-employee.component.scss']
})
export class AddEmployeeComponent implements OnInit, OnDestroy {
    constructor(
        private _departmentService: DepartmentServiceService,
        private _employeeService: EmployeeServiceService,
        private _loginService: LoginService,
        private _snackBar: MatSnackBar,
        private _router: Router,
        private dialog:MatDialog
    ) { }

    // Employee Form variables
    employeeForm: FormGroup | any;
    departmentData!: any;
    teamsData!: any;
    imgFormData!: FormData;

    // Api subscription Observables
    private _DeptapiSubscription!: Subscription;
    private _addEmpApiSubscription!: Subscription;
    private _setEmpImgApiSubscription!: Subscription;

    // Loaders
    isComponentLoading: boolean = false;
    isAddEmployeeProcessing: boolean = false;

    // other variables
    adminCode: string = "";
    employeeDetails = {
        profile_image: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png'
    }

    // Fetch all Department list
    loadDepartments() {
        this.isComponentLoading = true;
        this._DeptapiSubscription = this._departmentService
            .getDepartmentData()
            .subscribe({
                next: (res: any) => {
                    this.departmentData = res;
                },
                error: (error: HttpErrorResponse) => {
                    this.isComponentLoading = false;
                    if (error.status != 401) {
                        this._router.navigate(['something-went-wrong']);
                    }
                },
                complete: () => {
                    this.isComponentLoading = false;
                }
            });
    }

    // Fetch all teams based on department id
    getTeams(event: any) {
        this.isComponentLoading = true;
        this._DeptapiSubscription = this._departmentService
            .getAllTeams(event.target.value)
            .subscribe({
                next: (res: any) => {
                    this.teamsData = res;
                }, error: (error: HttpErrorResponse) => {
                    this.isComponentLoading = false;
                    if (error.status != 401) {
                        this._router.navigate(['something-went-wrong']);
                    }
                }, complete: () => {
                    this.isComponentLoading = false;
                }
            });
    }

    // load add employee form
    loadAddEmpForm() {
        this.employeeForm = new FormGroup(
            {
                profileImage: new FormControl(""),
                fullName: new FormControl("", [Validators.required, Validators.minLength(3)]),
                dateOfBirth: new FormControl("", [Validators.required]),
                empRole: new FormControl("", Validators.required),
                phoneNumber: new FormControl("", [Validators.required, Validators.pattern('[1-9]{1}[0-9]{9}')]),
                emailId: new FormControl("", [Validators.required, Validators.email]),
                gender: new FormControl("M"),

                emergencyNumber: new FormControl("", [Validators.required, Validators.pattern('[1-9]{1}[0-9]{9}')]),
                presentAddress: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(50)]),
                permenentAddress: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(50)]),
                panNo: new FormControl("", [Validators.required, Validators.pattern('[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}')]),
                aadharNo: new FormControl("", [Validators.required, Validators.pattern('[1-9]{1}[0-9]{11}')]),
                empCode: new FormControl("", [Validators.required, Validators.pattern('emp[0-9]+')]),
                departmentName: new FormControl("", [Validators.required]),
                reportingManager: new FormControl("", [Validators.required]),
                teamName: new FormControl("", [Validators.required]),
                empType: new FormControl("", [Validators.required]),
                actualEmployementDate: new FormControl("", [Validators.required]),
                pastExperience: new FormControl("", [Validators.required, Validators.min(0), Validators.max(40)]),
                dateOfJoining: new FormControl("", [Validators.required]),
                rating: new FormControl("", [Validators.required]),

                payrollCode: new FormControl("", [Validators.required, Validators.pattern('payroll[0-9]+')]),
                ctc: new FormControl("", [Validators.required, Validators.min(10000)]),
                hra: new FormControl("", [Validators.required]),
                pf: new FormControl("", [Validators.required]),
                basicSalary: new FormControl("", [Validators.required]),
                other: new FormControl("", [Validators.required])
            }
        );
    }

    ngOnInit(): void {
        this.loadDepartments()
        this.loadAddEmpForm();
        this._loginService.userdata.subscribe((res) => {
            this.adminCode = res.empCode;
        })
    }

    imgChange(event: any) {
        const file: File = event.target.files[0];
        this.imgFormData = new FormData();
        this.employeeDetails.profile_image = URL.createObjectURL(event.target.files[0]);
        this.imgFormData.append("profileImg", file, file.name)
    }

    onSubmit() {
        if (this.employeeForm.status === "VALID") {
            this.isAddEmployeeProcessing = true;
            const addEmpData = {
                profileImage: 'default.jpg',
                fullName: this.employeeForm.value.fullName.toLowerCase(),
                dateOfBirth: this.employeeForm.value.dateOfBirth,
                roleId: Number(this.employeeForm.value.empRole),
                phoneNumber: String(this.employeeForm.value.phoneNumber),
                emailId: this.employeeForm.value.emailId,
                gender: String(this.employeeForm.value.gender),
                emergencyNumber: String(this.employeeForm.value.emergencyNumber),
                panNo: this.employeeForm.value.panNo,
                aadharNo: String(this.employeeForm.value.aadharNo),
                presentAddress: this.employeeForm.value.presentAddress,
                permanentAddress: this.employeeForm.value.permenentAddress,
                empCode: String(this.employeeForm.value.empCode.toLowerCase()),
                departmentId: Number(this.employeeForm.value.departmentName),
                teamId: Number(this.employeeForm.value.teamName),
                reportingManager: this.employeeForm.value.reportingManager,
                empType: Number(this.employeeForm.value.empType),
                actualEmployementDate: this.employeeForm.value.actualEmployementDate,
                pastExperience: String(this.employeeForm.value.pastExperience),
                dateOfJoining: this.employeeForm.value.dateOfJoining,
                rating: Number(this.employeeForm.value.rating),

                payrollCode: this.employeeForm.value.payrollCode,
                ctc: String(this.employeeForm.value.ctc),
                hra: String(this.employeeForm.value.hra),
                pf: String(this.employeeForm.value.pf),
                basicSalary: String(this.employeeForm.value.basicSalary),
                other: String(this.employeeForm.value.other),
            };

            this._loginService.userdata.subscribe(res => {
                this.adminCode = res.empCode;
            })

            this._addEmpApiSubscription = this._employeeService
                .addEmployeeDetails(
                    this.adminCode,
                    { raise: "0", ...addEmpData }
                ).subscribe({
                    next: (response: any) => {
                        this._setEmpImgApiSubscription = this._employeeService.uploadImage(response.empId, this.adminCode, this.imgFormData).subscribe(
                            {
                                next: (res) => {
                                    console.log(res);
                                },
                                error: (err: HttpErrorResponse) => {
                                    console.log(err);
                                    if(err.status!=401){
                                        this._router.navigate(['something-went-wrong'])
                                    }else{
                                        this.dialog.open(ErrorComponent, {
                                            panelClass: 'modal',

                                            width: '500px',
                                            backdropClass: 'backdropBackground',
                                            disableClose: true,
                                        });
                                    }
                                },
                                complete: () => {
                                    // Emp Added and Img uploaded
                                }
                            })
                        this.isAddEmployeeProcessing = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.isAddEmployeeProcessing = false;
                        console.log(error);
                        if(error.status == 400){
                            this._snackBar.open("Empcode already exist or duplicate entry!", "Close", {
                                duration: 3000,
                                panelClass: ['green-snackbar', 'login-snackbar'],
                            })
                        }else{
                            this._snackBar.open("Something Went wrong!", "Close", {
                                duration: 3000,
                                panelClass: ['green-snackbar', 'login-snackbar'],
                            })
                        }
                    },
                    complete: () => {
                        this.isAddEmployeeProcessing = false;
                        this._snackBar.open("Employee Added Successfully!", "Close", {
                            duration: 3000,
                            panelClass: ['green-snackbar', 'login-snackbar'],
                        })
                        this._router.navigate(['employees'])
                    },
                });
        }
        else {
            this._snackBar.open("Please fill form properly!", "Close", {
                duration: 3000,
                panelClass: ['green-snackbar', 'login-snackbar'],
            })
        }
    }
    consoleRating(e: Event) {
        console.log((e.target as HTMLInputElement).value);
    }

    calculateSalary() {
        if (this.employeeForm.get('ctc').status === "VALID") {
            let ctc = this.employeeForm.get('ctc').value;
            this.employeeForm.get('hra').setValue(ctc * 40 / 100);
            this.employeeForm.get('pf').setValue(ctc * 12 / 100);
            this.employeeForm.get('basicSalary').setValue(ctc * 40 / 100);
            this.employeeForm.get('other').setValue(ctc * 8 / 100);
        } else {
            this.employeeForm.get('hra').setValue(0);
            this.employeeForm.get('pf').setValue(0);
            this.employeeForm.get('basicSalary').setValue(0);
            this.employeeForm.get('other').setValue(0);
        }
    }

    ngOnDestroy(): void {
        if (this._DeptapiSubscription) {
            this._DeptapiSubscription.unsubscribe();
        }
        if (this._addEmpApiSubscription) {
            this._addEmpApiSubscription.unsubscribe();
        }
        if (this._setEmpImgApiSubscription) {
            this._setEmpImgApiSubscription.unsubscribe();
        }
    }
}
