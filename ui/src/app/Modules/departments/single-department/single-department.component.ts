import { Component } from '@angular/core';
import { ActivatedRoute, Params, Router, RouterEvent } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AddTeamComponent } from './team/add-team/add-team.component';
import { AddDepartmentComponent } from '../add-department/add-department.component';
import { DepartmentServiceService } from '../services/department-service.service';
import { ConfirmComponent } from '../../shared/components/confirm/confirm.component';
import { HttpErrorResponse } from '@angular/common/http';
import { teamList } from '../Model/team.model';
import { ChartService } from '../../shared/Services/chart-service.service';
import { ChartModel } from '../../shared/model/chart-model';
import { ErrorComponent } from '../../shared/components/error/error.component';
@Component({
    selector: 'app-single-department',
    templateUrl: './single-department.component.html',
    styleUrls: ['./single-department.component.scss'],
})
export class SingleDepartmentComponent {
    public isLoading = false;
    public teamList!: teamList[];
    public payrollChart: any;
    public departmentId!: number;
    public departmentName: string;
    public departmentsData: any;
    private _departmentApiSubscription: any;
    public departmentWiseCostingChart: any;

    constructor(
        private route: ActivatedRoute,
        private navi: Router,
        private dialog: MatDialog,
        private _departmentSevice: DepartmentServiceService,
        private _chartService: ChartService
    ) {
        this.departmentName = '';
    }

    ngOnInit(): void {
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        this.route.params.subscribe((params: Params) => {
            this.departmentId = +params['id'];
        });
        this._departmentSevice.departmentData.subscribe((res: any) => {
            res.forEach((element: any) => {
                if (element.departmentId === this.departmentId) {
                    this.departmentName = element.departmentName;
                }
            });
        });
        this._departmentApiSubscription = this._departmentSevice
            .getAllTeams(this.departmentId)
            .subscribe({
                next: (response: any) => {
                    this.teamList = response;
                    this.createChart();
                },
                error: (error: HttpErrorResponse) => {
                    if (error.status != 401) {
                        this.navi.navigate(['something-went-wrong']);
                    } else {
                        this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',
                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                },
                complete: () => {
                    this.isLoading = false;
                },
            });
    }

    addTeam() {
        const dialogRef = this.dialog.open(AddTeamComponent, {
            panelClass: 'modal',
            data: {
                title: 'Add Team',
                edit: false,
                departmentId: this.departmentId,
            },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result.reload) {
                this.loadData();
            }
        });
    }

    editDepartment(id: any) {
        const dialogRef = this.dialog.open(AddDepartmentComponent, {
            panelClass: 'modal',
            data: { title: 'Edit Department', edit: true, departmentId: id },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result.reload) {
                this.loadData();
            }
        });
    }

    deleteDepartment(id: any) {
        const dialogRef = this.dialog.open(ConfirmComponent, {
            panelClass: 'modal',
            data: {
                title: 'Delete Department',
                name: this.departmentName,
                departmentId: id,
            },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
        dialogRef.afterClosed().subscribe((result) => {
            if (result.reload) {
                this.navi.navigate([`departments`]);
            }
        });
    }

    openDialog(component: any, title: string): void {
        const dialogRef = this.dialog.open(component, {
            panelClass: 'modal',
            data: { title },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
    }

    createChart() {
        let temp = this.teamList.map((item) => ({
            name: item.teamName,
            y: +item.costing,
        }));
        console.log(temp);
        let tempObj: ChartModel = {
            title: 'Team Wise Expense',
            colors: ['blue'],
            innerData: temp,
        };
        this.departmentWiseCostingChart =
            this._chartService.getBarChart(tempObj);
    }
    
    ngOnDestroy() {
        this._departmentApiSubscription.unsubscribe();
    }
}
