import { Component, HostListener } from '@angular/core';
import { DashboardServiceService } from '../services/dashboard-service.service';
import { DashboardModel } from '../model/dashboard-model';
import { ChartService } from '../../shared/Services/chart-service.service';
import { ChartModel } from '../../shared/model/chart-model';
import { DepartmentServiceService } from '../../departments/services/department-service.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ErrorComponent } from '../../shared/components/error/error.component';

@Component({
    selector: 'app-dashboard-component',
    templateUrl: './dashboard-component.component.html',
    styleUrls: ['./dashboard-component.component.scss'],
})
export class DashboardComponentComponent {
    public departmentList!: any[];
    private _dashboardData!: DashboardModel;
    public isdropdownLoading: boolean = false;
    public isLoading = true;
    public dashCardData: any;
    public genderChart: any;
    public salaryChart: any;

    constructor(
        private _dashboardApi: DashboardServiceService,
        private _chartService: ChartService,
        private _departmentApi: DepartmentServiceService,
        private dialog: MatDialog,
        private router: Router
    ) {}

    ngOnInit(): void {
        this.isLoading = true;
        //setting all dashboard data
        let dashboardApiSubscription = this._dashboardApi
            .getDashboardData()
            .subscribe({
                next: (dashboard) => {
                    this._dashboardData = dashboard;
                    this.setData();
                    this.createChart();
                },
                error: (error) => {
                    if (error.status != 401) {
                        this.router.navigate(['something-went-wrong']);
                    } else {
                        const dialogRef = this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',
                            data: {},
                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                },
                complete: () => {
                    dashboardApiSubscription.unsubscribe();
                },
            });
    }
    
    //for dropdown menu items
    @HostListener('mouseover', ['$event.target.id'])
    onDropdownBtnClick($event: any) {
        if ($event == 'dropdown-btn' && !this.departmentList) {
            this.isdropdownLoading = true;
            let departmentApiSubscription = this._departmentApi
                .getDepartmentData()
                .subscribe({
                    next: (departments) => {
                        this.departmentList = departments;
                    },
                    error: (error) => {
                        if (error.status! == 401) {
                            this.router.navigate(['something-went-wrong']);
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
                        this.isdropdownLoading = false;
                        departmentApiSubscription.unsubscribe();
                    },
                });
        }
    }

    createChart() {
        //for changing the format of the data that is needed to pass to the chart
        let changeData = (innerData: any) => {
            let tempArr = [];
            for (let [key, value] of Object.entries(innerData)) {
                tempArr.push({ name: key, y: value });
            }
            return tempArr;
        };

        let salary: ChartModel = {
            colors: ['#4b49ac'],
            title: 'Year Wise Expense',
            innerData: <{ name: string; y: any }[]>(
                changeData(this._dashboardData.yearWiseTotalPayout)
            ),
            yAxisText: 'Total Expense',
        };

        let gender: ChartModel = {
            colors: ['rgb(95,167,79)', 'rgb(76,177,241)'],
            title: 'Gender Ratio',
            innerData: <{ name: string; y: any }[]>changeData({
                Male: this._dashboardData.totalMale,
                Female: this._dashboardData.totalFemale,
            }),
        };

        //settinng the graph
        this.salaryChart = this._chartService.getBarChart(salary);
        this.genderChart = this._chartService.getPieChart(gender);

        this.isLoading = false;
    }

    setData() {
        //dashboard card data
        this.dashCardData = [
            {
                routeLink: '/departments',
                icon: 'fas fa-building',
                title: 'Total Departments',
                data: this._dashboardData.totalDepartments,
                colorClass: 'green',
            },
            {
                routeLink: '/employees',
                icon: 'fa-solid fa-briefcase',
                title: 'Total Employees',
                data: this._dashboardData.totalEmployees,
                colorClass: 'blue',
            },
            {
                itsCurrency: true,
                routeLink: '/reports',
                icon: 'fas fa-sack-dollar',
                title: 'Total Costing',
                data: this._dashboardData.totalPayout,
                colorClass: 'red',
            },
            {
                routeLink: '/departments',
                icon: 'fad fa-users-class',
                title: 'Total Teams',
                data: this._dashboardData.totalTeams,
                colorClass: 'black',
            },
        ];
    }
}
