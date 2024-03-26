import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EmployeeServiceService } from '../services/employee-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { EmpBasicDetails } from '../models/EmpBasicDetails.model';
import { ActivatedRoute, Router } from '@angular/router';
import { DepartmentServiceService } from '../../departments/services/department-service.service';
import { LoginService } from 'src/app/services/login.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-single-employee',
    templateUrl: './single-employee.component.html',
    styleUrls: ['./single-employee.component.scss']
})
export class SingleEmployeeComponent implements OnInit, OnDestroy {

    // Employee Form variables
    employeeForm: FormGroup | any;
    empId!: number;

    // Loaders
    isComponentLoading: boolean = false;
    isUpdateProcessing: boolean = false;
    empDataFetching: boolean = false;

    // Error check
    basicFormError: boolean = false;

    // Api subscription Observables
    _deptApiSubscription!: Subscription;
    _tempsApiSubscription!: Subscription;
    _empApiSubscription!: Subscription;

    // other variables for form
    departmentData: any;
    teamsData: any;
    emp_profile_image: string;
    adminCode!: string;
    new_annual_ctc: number;
    new_hra: number;
    new_pf: number;
    new_base_salary: number;
    new_other_taxes: number;
    raise_percentage: number;
    imgFormData!: FormData;
    profile_image!: string;

    // variables for api hit
    ratingId!: number;
    salaryID!: number;


    constructor(
        private _empService: EmployeeServiceService,
        private _loginService: LoginService,
        private _departmentService: DepartmentServiceService,
        private _snackBar: MatSnackBar,
        private _activatedRoute: ActivatedRoute,
        private _router: Router,
    ) {
        this.emp_profile_image = "";
        this._loginService.userdata.subscribe(res => {
            this.adminCode = res.empCode;
        })
        this.raise_percentage = 0;
        this.new_annual_ctc = 0;
        this.new_hra = 0;
        this.new_pf = 0;
        this.new_base_salary = 0;
        this.new_other_taxes = 0;

        this._activatedRoute.params.subscribe(params => {
            this.empId = params['id'];
        })
    }

    // Fetch all Department list
    loadDepartments() {
        this.isComponentLoading = true;
        this._deptApiSubscription = this._departmentService
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
        this._tempsApiSubscription = this._departmentService
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

    fetchEmployeeData() {
        this.empDataFetching = true;

        this._empApiSubscription = this._empService.fetchSingleEmpBasic(this.empId).subscribe({
            next: (data: EmpBasicDetails) => {
                this.empDataFetching = false;
                this.basicFormError = false;
                this.emp_profile_image = data.profileImage;
                this.ratingId = data.ratingId;
                this.salaryID = data.salaryID;

                this.employeeForm = new FormGroup({
                    profile_image: new FormControl(data.profileImage),
                    full_name: new FormControl(data.fullName, [Validators.required, Validators.minLength(3)]),
                    date_of_birth: new FormControl(data.dateOfBirth, Validators.required),
                    emp_role: new FormControl(data.roleId, Validators.required),
                    phone_number: new FormControl(data.phoneNumber, [Validators.required, Validators.pattern('[1-9]{1}[0-9]{9}')]),
                    email_id: new FormControl(data.emailId, [Validators.required, Validators.email]),
                    gender: new FormControl(data.gender, Validators.required),

                    emergency_number: new FormControl(data.emergencyNumber, [Validators.required, Validators.pattern('[1-9]{1}[0-9]{9}')]),
                    present_address: new FormControl(data.presentAddress, [Validators.required, Validators.minLength(10), Validators.maxLength(50)]),
                    permenent_address: new FormControl(data.permanentAddress, [Validators.required, Validators.minLength(10), Validators.maxLength(50)]),
                    pan_no: new FormControl(data.panNo, [Validators.required, Validators.pattern('[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}')]),
                    aadhar_no: new FormControl(data.aadharNo, [Validators.required, Validators.pattern('[1-9]{1}[0-9]{11}')]),
                    emp_code: new FormControl(data.empCode, [Validators.required, Validators.pattern('emp[0-9]+')]),
                    department_name: new FormControl(data.departmentId, Validators.required),
                    reporting_manager: new FormControl(data.reportingManager, Validators.required),
                    team_name: new FormControl(data.teamId, Validators.required),
                    emp_type: new FormControl((data.empType === "PERMANENT" ? "1" : "0"), Validators.required),
                    actual_employement_date: new FormControl(data.actualEmployementDate, Validators.required),
                    past_experience: new FormControl(data.pastExperience, [Validators.required, Validators.min(0), Validators.max(40)]),
                    date_of_joining: new FormControl(data.dateOfJoining, Validators.required),
                    prev_rating: new FormControl(String(data.rating)),
                    new_rating: new FormControl(0),
                    payroll_code: new FormControl(data.payrollCode, [Validators.required, Validators.pattern('payroll[0-9]+')]),
                    annual_ctc: new FormControl(data.ctc, [Validators.required]),
                    hra: new FormControl(data.hra, [Validators.required]),
                    pf: new FormControl(data.pf, [Validators.required]),
                    base_salary: new FormControl(data.basicSalary, [Validators.required]),
                    other_taxes: new FormControl(data.other, [Validators.required]),

                    raise_percentage: new FormControl("", [Validators.required, Validators.pattern('^(0?[0-9]|[1-9][0-9])$'), Validators.min(0), Validators.max(100)]),

                    new_annual_ctc: new FormControl(this.new_annual_ctc, [Validators.required]),
                    new_hra: new FormControl(this.new_hra, [Validators.required]),
                    new_pf: new FormControl(this.new_pf, [Validators.required]),
                    new_base_salary: new FormControl(this.new_base_salary, [Validators.required]),
                    new_other_taxes: new FormControl(this.new_other_taxes, [Validators.required])
                });

                this._departmentService
                    .getAllTeams(data.departmentId)
                    .subscribe((response) => {
                        this.teamsData = response;
                    });
            },
            error: (error: HttpErrorResponse) => {
                this.empDataFetching = false;
                this.basicFormError = false;
                console.error("An error Occurred ", error);
            },
            complete: () => {
                console.log("Fetching Single Emp details complete");
            }

        })
    }

    ngOnInit(): void {
        this.loadDepartments();
        this.fetchEmployeeData()
    }

    imgChange(event: any) {
        const file: File = event.target.files[0];
        this.imgFormData = new FormData();
        this.profile_image = URL.createObjectURL(event.target.files[0]);
        this.imgFormData.append("profileImg", file, file.name)
    }

    deleteEmployee() {
        this._empApiSubscription = this._empService.deleteEmployeeDetails(this.empId, this.adminCode).subscribe({
            next: (res) => {
                console.log(res)
                this._router.navigate(['/employees']);
            },
            error: (err) => {
                console.log(err)
            },
            complete: () => {
                console.log("Employee Deleted");
            }
        })
    }

    onSubmit() {
        if (this.employeeForm.status === "VALID") {
            this.isUpdateProcessing = true;
            const updateEmpData = {
                profileImage: 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460__340.png',
                fullName: this.employeeForm.value.full_name,
                dateOfBirth: this.employeeForm.value.date_of_birth,
                roleId: Number(this.employeeForm.value.emp_role),
                phoneNumber: String(this.employeeForm.value.phone_number),
                emailId: this.employeeForm.value.email_id,
                gender: String(this.employeeForm.value.gender),
                emergencyNumber: String(this.employeeForm.value.emergency_number),
                panNo: this.employeeForm.value.pan_no,
                aadharNo: String(this.employeeForm.value.aadhar_no),
                presentAddress: this.employeeForm.value.present_address,
                permanentAddress: this.employeeForm.value.permenent_address,
                empCode: String(this.employeeForm.value.emp_code),
                departmentId: Number(this.employeeForm.value.department_name),
                teamId: Number(this.employeeForm.value.team_name),
                reportingManager: this.employeeForm.value.reporting_manager,
                empType: String(this.employeeForm.value.emp_type),
                actualEmployementDate: this.employeeForm.value.actual_employement_date,
                pastExperience: String(this.employeeForm.value.past_experience),
                dateOfJoining: this.employeeForm.value.date_of_joining,
                rating: Number(this.employeeForm.value.new_rating),
                raise: String(this.employeeForm.value.raise),

                payrollCode: this.employeeForm.value.payroll_code,
                ctc: String(this.employeeForm.value.new_annual_ctc),
                hra: String(this.employeeForm.value.new_hra),
                pf: String(this.employeeForm.value.new_pf),
                basicSalary: String(this.employeeForm.value.new_base_salary),
                other: String(this.employeeForm.value.new_other_taxes),
            };

            this._empApiSubscription = this._empService.updateEmployeeDetails(this.adminCode, {
                empId: Number(this.empId),
                ratingId: Number(this.ratingId),
                salaryID: Number(this.salaryID),
                ...updateEmpData
            }).subscribe({
                next: (response) => {
                    this.isUpdateProcessing = false;
                    if (this.employeeForm.value.raise !== 0) {
                        this._snackBar.open("Employee Updated Successfully & Mail sent!", "Close", {
                            duration: 3000,
                            panelClass: ['green-snackbar', 'login-snackbar'],
                        })
                    } else {
                        this._snackBar.open("Employee Updated Successfully!", "Close", {
                            duration: 3000,
                            panelClass: ['green-snackbar', 'login-snackbar'],
                        })
                    }
                    this._router.navigate(['/employees']);
                },
                error: (err) => {
                    this.isUpdateProcessing = false;
                    console.log(err);
                },
                complete: () => {
                    console.log("Employee Updated Successfully");
                }
            })
        } else {
            this._snackBar.open("Please fill form properly!", "Close", {
                duration: 3000,
                panelClass: ['green-snackbar', 'login-snackbar'],
            })
        }
    }

    calculateSalary(event: any) {
        if (this.employeeForm.get('raise_percentage').status === "VALID") {
            let raisePercentage = event.target.value;
            let oldCtc = this.employeeForm.get('annual_ctc').value;
            let newCtc = Number(oldCtc) + (oldCtc * raisePercentage / 100);

            this.employeeForm.get('new_annual_ctc').setValue(newCtc);
            this.employeeForm.get('new_hra').setValue(newCtc * 40 / 100);
            this.employeeForm.get('new_pf').setValue(newCtc * 12 / 100);
            this.employeeForm.get('new_base_salary').setValue(newCtc * 40 / 100);
            this.employeeForm.get('new_other_taxes').setValue(newCtc * 8 / 100);
        } else {
            this.employeeForm.get('new_annual_ctc').setValue(0);
            this.employeeForm.get('new_hra').setValue(0);
            this.employeeForm.get('new_pf').setValue(0);
            this.employeeForm.get('new_base_salary').setValue(0);
            this.employeeForm.get('new_other_taxes').setValue(0);
        }
    }

    ngOnDestroy(): void {
        if (this._deptApiSubscription) {
            this._deptApiSubscription.unsubscribe();
        }
        if (this._empApiSubscription) {
            this._empApiSubscription.unsubscribe();
        }
        if (this._tempsApiSubscription) {
            this._tempsApiSubscription.unsubscribe();
        }
    }
}
