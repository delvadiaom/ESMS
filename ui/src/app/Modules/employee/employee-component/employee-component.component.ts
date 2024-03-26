import { HttpErrorResponse } from '@angular/common/http';
import {
    AfterViewInit,
    Component,
    ElementRef,
    OnDestroy,
    OnInit,
    ViewChild,
} from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { EmployeeServiceService } from '../services/employee-service.service';
import { Subscription } from 'rxjs';
import { EmpData } from '../models/EmpData.model copy';
import { DepartmentServiceService } from '../../departments/services/department-service.service';
import { MatDialog } from '@angular/material/dialog';
import { SingleUserActivityLogComponent } from '../../activitylog/activitylog-compoent/single-user-activity-log/single-user-activity-log.component';
import { MatSlideToggle } from '@angular/material/slide-toggle';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-employee-component',
    templateUrl: './employee-component.component.html',
    styleUrls: ['./employee-component.component.scss'],
})
export class EmployeeComponentComponent implements OnInit, AfterViewInit, OnDestroy {
    public departmentList!: any[];
    public teamList!: any[];

    // Observables for Api Subscription
    private _departmentApiObs!: Subscription;
    private _empListApiObs!: Subscription;
    private _empCountApiObs!: Subscription;

    // Loaders
    isComponentLoading: boolean = false;
    isDataLoading: boolean = true;
    isDepartmentLoading: boolean = false;
    isTeamLoading: boolean = false;
    isEmpStatusLoading: boolean = false;

    // filters
    selectedDepartment: string = 'all';
    selectedTeam: string = 'all';
    filterNameInput: string = '';

    // variables for Api
    adminCode!: string;
    deleteEmpProcessing!: number;

    emps: EmpData[] = [
        {
            no: 0,
            aadharNo: '',
            actualEmployementDate: 0,
            createdDate: 0,
            dateOfBirth: 0,
            dateOfJoining: 0,
            departmentId: 0,
            departmentName: '',
            emailId: '',
            emergencyNumber: '',
            empCode: '',
            empId: 0,
            empType: 0,
            fullName: '',
            gender: '',
            isActive: false,
            modifiedDate: 0,
            panNo: '',
            pastExperience: 0,
            permanentAddress: '',
            phoneNumber: '',
            presentAddress: '',
            profileImage: '',
            reportingManager: '',
            roleId: 0,
            roleName: '',
            teamId: 0,
            teamName: '',
        },
    ];

    // MatTable Variables
    empCount: number = 0;
    totalRows: number = 0;
    pageSize: number = 10;
    currentPage: number = 0;
    pageSizeOptions: number[] = [10, 20, 40];
    dataSource: MatTableDataSource<EmpData> = new MatTableDataSource();
    displayedColumns: string[] = [
        'empCode',
        'fullName',
        'departmentName',
        'teamName',
        'roleName',
        'isActive',
        'log',
        'detail',
    ];

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;
    @ViewChild('searchInput') searchInput!: ElementRef;

    constructor(
        private _snackBar: MatSnackBar,
        private _empService: EmployeeServiceService,
        private _loginService: LoginService,
        private _departmentservice: DepartmentServiceService,
        private _router: Router,
        public dialog: MatDialog,
    ) { }

    // Fetch employee count for pagination
    fetchEmpCount() {
        this._empCountApiObs = this._empService.fetchEmpsCount().subscribe({
            next: (res: number) => {
                this.empCount = res;
            },
            error: (error) => {
                console.log(error);
            },
            complete: () => {
                console.log('done');
            },
        });
    }

    // Fetch employees with pagination and limit
    fetchEmployees() {
        this.fetchEmpCount()
        this.isDataLoading = true;
        this._empListApiObs = this._empService
            .fetchEmpsData(this.currentPage, this.pageSize)
            .subscribe({
                next: (response: EmpData[]) => {
                    this.dataSource = new MatTableDataSource(response);
                    setTimeout(() => {
                        this.paginator.pageIndex = this.currentPage;
                        this.paginator.length = this.empCount;
                        this.dataSource.sort = this.sort;
                    }, 1000);
                },
                error: (error: HttpErrorResponse) => {
                    this.isDataLoading = false;
                    this.isComponentLoading = false;
                    console.log(error);
                    if (error.status != 401) {
                        this._router.navigate(['something-went-wrong']);
                    }
                },
                complete: () => {
                    this.isDataLoading = false;
                    this.isComponentLoading = false;
                    console.log('Success!!');
                },
            });
    }

    // Fetch department list for filter
    fetchDepartments() {
        this._departmentApiObs = this._departmentservice
            .getDepartmentData()
            .subscribe({
                next: (res: any) => {
                    this.departmentList = res;
                    this.isDepartmentLoading = false;
                },
                error: (err: Error) => {
                    console.log(err);
                    this.isDepartmentLoading = false;
                },
                complete: () => {
                    console.log('Department Fetched!');
                    this.isDepartmentLoading = false;
                },
            });
    }

    ngOnInit(): void {
        this.isComponentLoading = true;
        this.fetchEmpCount();
        this.fetchEmployees();
        this.fetchDepartments();

        // Subscribing behaviour Subject of current user
        this._loginService.userdata.subscribe((res) => {
            this.adminCode = res.empCode;
        });
    }

    ngAfterViewInit() {
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
    }

    // Fetch team based on dept-Id for filter
    fetchTeams(deptId: number) {
        console.log(deptId);
        this.isTeamLoading = true;
        this._departmentservice.getAllTeams(deptId).subscribe({
            next: (res: any) => {
                this.teamList = res;
                this.isTeamLoading = false;
            },
            error: (err: Error) => {
                console.log(err);
                this.isTeamLoading = false;
            },
            complete: () => {
                console.log('Teams Fetched!');
                this.isTeamLoading = false;
            },
        });
    }

    setSelectedDepartment(event: any) {
        this.selectedDepartment = event.target.value;
        this.fetchByDepartmentId();
    }

    setSelectedTeam(event: any) {
        this.selectedTeam = event.target.value;
        this.fetchByTeamId();
    }

    // API call based on dept filter
    fetchByDepartmentId() {
        this.dataSource = new MatTableDataSource();

        if (this.selectedDepartment !== 'all') {
            this.isDepartmentLoading = true;
            this.isDataLoading = true;
            this.fetchTeams(Number(this.selectedDepartment));

            this._empService
                .filterDatabyDepartmentId(Number(this.selectedDepartment))
                .subscribe({
                    next: (res: any) => {
                        let empData = res;
                        // If search box has some value then filter based on it.
                        if (this.searchInput.nativeElement.value) {
                            empData = res.filter((emp: any) => {
                                return emp.fullName.includes(
                                    this.searchInput.nativeElement.value
                                );
                            });
                        }
                        this.dataSource = empData;
                        this.isDataLoading = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        console.log(error);
                        this.isDepartmentLoading = false;
                    },
                    complete: () => {
                        this.isDepartmentLoading = false;
                        this.isDataLoading = false;
                        console.log('success');
                    },
                });
        } else {
            this.searchInput.nativeElement.value = '';
            this.fetchEmployees();
        }
    }

    // API call based on team filter
    fetchByTeamId() {
        this.dataSource = new MatTableDataSource();
        if (this.selectedTeam !== 'all') {
            this.isTeamLoading = true;
            this.isDataLoading = true;

            this._empService
                .filterDatabyTeamId(Number(this.selectedTeam))
                .subscribe({
                    next: (res: any) => {
                        let empData = res;
                        // If search box has some value then filter based on it.
                        if (this.searchInput.nativeElement.value) {
                            empData = res.filter((emp: any) => {
                                return emp.fullName.includes(
                                    this.searchInput.nativeElement.value
                                );
                            });
                        }
                        this.dataSource = empData;
                    },
                    error: (error: HttpErrorResponse) => {
                        console.log(error);
                        this.isDataLoading = false;
                        this.isTeamLoading = false;
                    },
                    complete: () => {
                        this.isTeamLoading = false;
                        this.isDataLoading = false;
                        console.log('success');
                    },
                });
        } else {
            this.searchInput.nativeElement.value = '';
            if (this.selectedDepartment !== 'all') {
                this.fetchByDepartmentId();
            } else {
                this.fetchEmployees();
            }
        }
    }

    // Apply filter based on keyword
    searchEmployee(searchInput: HTMLInputElement) {
        this.dataSource = new MatTableDataSource();
        this.isDataLoading = true;

        this._empService.filterDataByName(searchInput.value).subscribe({
            next: (res: any) => {
                let empData = res;
                if (this.selectedDepartment !== 'all') {
                    empData = res.filter((emp: any) => {
                        return (
                            emp.departmentId === Number(this.selectedDepartment)
                        );
                    });
                    if (this.selectedTeam !== 'all') {
                        empData = empData.filter((emp: any) => {
                            return emp.teamId === Number(this.selectedTeam);
                        });
                    }
                }
                this.dataSource = empData;
            },
            error: (error: HttpErrorResponse) => {
                console.log(error);
                this.isDataLoading = false;
            },
            complete: () => {
                this.isDataLoading = false;
                console.log('success');
            },
        });
    }

    // Page Change
    pageChanged(event: PageEvent) {
        this.dataSource = new MatTableDataSource();
        this.pageSize = event.pageSize;
        this.currentPage = event.pageIndex;
        this.fetchEmployees();
    }

    openDialog(code: string): void {
        const dialogRef = this.dialog.open(SingleUserActivityLogComponent, {
            data: code + ' 1',
            panelClass: 'modal',
            backdropClass: 'backdropBackground',
            width: '90vw',
            disableClose: true,
            maxHeight: '98vh',
        });
    }

    // Active / Deactive Employee
    onChangeStatus(
        event: MatSlideToggleChange,
        empId: number,
        switchRef: MatSlideToggle
    ) {
        this.isEmpStatusLoading = true;
        this.deleteEmpProcessing = empId;

        console.log(event.checked);

        if (event.checked) {
            if (confirm('Are you sure you want to Activate Employee')) {
                this._empService.activateEmployee(empId, this.adminCode).subscribe({
                    next: (res) => {
                        this.isEmpStatusLoading = false;
                        this.fetchEmployees()
                    },
                    error: (err) => {
                        console.log(err);
                        this.fetchEmployees()
                        this.isEmpStatusLoading = false;
                        this._snackBar.open('Something went wrong!', 'Close', {
                            duration: 3000,
                            panelClass: ['red-snackbar'],
                        });
                        console.log(switchRef.checked);
                    },
                    complete: () => {
                        console.log('Employee Activated');
                    },
                });
            } else {
                switchRef.checked = false;
                this.isEmpStatusLoading = false;
            }
        } else {
            if (confirm('Are you sure you want to Deactivate Employee')) {
                this._empService
                    .deleteEmployeeDetails(empId, this.adminCode)
                    .subscribe({
                        next: (res) => {
                            this.isEmpStatusLoading = false;
                            this.fetchEmployees()
                        },
                        error: (err) => {
                            console.log(err);
                            this.fetchEmployees();
                            this.isEmpStatusLoading = false;
                            this._snackBar.open(
                                'Something went wrong!',
                                'Close',
                                {
                                    duration: 3000,
                                    panelClass: [
                                        'red-snackbar'
                                    ],
                                }
                            );
                        },
                        complete: () => {
                            console.log('Employee Deleted');
                        },
                    });
            } else {
                switchRef.checked = true;
                this.isEmpStatusLoading = false;
            }
        }
    }

    // Unsubscribing all the observables
    ngOnDestroy(): void {
        if (this._empListApiObs) {
            this._empListApiObs.unsubscribe();
        }
        if (this._departmentApiObs) {
            this._departmentApiObs.unsubscribe();
        }
        if (this._empCountApiObs) {
            this._empCountApiObs.unsubscribe();
        }
    }
}
