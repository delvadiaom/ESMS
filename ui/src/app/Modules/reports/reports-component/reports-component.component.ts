import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
// import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import * as XLSX from 'xlsx';
import { jsPDF } from 'jspdf';
import html2canvas from 'html2canvas';
import { ReportServiceService } from '../services/report-service.service';
import { ChartService } from '../../shared/Services/chart-service.service';
import {
    ExperinceWiseSalaryModel,
    TeamWiseCosting,
} from '../Model/reports.model';
import { ChartModel } from '../../shared/model/chart-model';
import { DepartmentServiceService } from '../../departments/services/department-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';
import { MatDialog } from '@angular/material/dialog';

interface EmpData {
    no: number;
    id: number;
    name: string;
    department_name: string;
    team_name: string;
    status: string;
}
//future need
// interface DepartmentData {
//     active: boolean;
//     costing: string;
//     departmentCode: string;
//     departmentName: string;
//     headedBy: string;
//     headedByName: string;
// }

@Component({
    selector: 'app-reports-component',
    templateUrl: './reports-component.component.html',
    styleUrls: ['./reports-component.component.scss'],
})
export class ReportsComponentComponent {
    private _colors = [
        '#47a7ce',
        '#549df9',
        '#8ea5cc',
        '#f2a47d',
        '#e6bab1',
        'rgb(247, 163, 92)',
        'rgb(128,133,233)',
        'rgb(144, 237, 125)',
        'rgb(124, 181, 236)',
    ]; //graph colors
    
    public isLoading: boolean = false;
    public isGraphLoaded: boolean = false;
    public departmentWiseExpenseChart: any;
    public experienceWiseSalaryChart: any;
    public teamWiseCostingChart: any;
    public graphsContainer: any = [];
    public show: boolean = false;
    public downloadType = [
        { name: 'Export to Excel', value: 'xls' },
        // { name: 'Export to PDF', value: 'pdf' },
    ];
    public downloadName: string = 'Select Download Type';
    public reportName: string = 'Department Wise Report';
    public deptName!: string;
    public departmentDropdown!: any[];
    public ReportType = [
        { name: 'Department Wise Report', value: 'genrateBydept' },
        { name: 'Individual Department', value: 'genrateByChart' },
    ];
    displayedColumns: string[] = [
        'departmentName',
        'departmentCode',
        'headedBy',
        'headedByName',
        'costing',
        'active',
    ];
    headerName: string[] = [
        'Department Name',
        'Department Code',
        'Head Code',
        'Head Name',
        'Costing',
        'Status',
    ];
    public dataSource: MatTableDataSource<EmpData>;

    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        private _reportAPIService: ReportServiceService,
        private _chartService: ChartService,
        private _getDepartmentList: DepartmentServiceService,
        private router: Router,
        private dialog: MatDialog
    ) {
        this.dataSource = new MatTableDataSource();
    }

    ngOnInit() {
        this.isLoading = true;
        this.isGraphLoaded = true;
        this.createLineChart();
        this.setAllDepartmentReport();
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    // Download Excel File
    xls(): void {
        let element = document.getElementById('dpdf');
        const ws: XLSX.WorkSheet = XLSX.utils.table_to_sheet(element);

        const wb: XLSX.WorkBook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

        XLSX.writeFile(
            wb,
            'Report_' +
                new Date().getDate() +
                '-' +
                new Date().getMonth() +
                '-' +
                new Date().getFullYear() +
                '.xlsx'
        );
    }
    // Download PDF File
    pdf() {
        let DATA: any = document.getElementById('download-pdf');
        html2canvas(DATA).then(
            (canvas: {
                height: number;
                width: number;
                toDataURL: (arg0: string) => any;
            }) => {
                let fileWidth = 208;
                let fileHeight = (canvas.height * fileWidth) / canvas.width;
                const FILEURI = canvas.toDataURL('image/png');
                let PDF = new jsPDF('p', 'mm', 'a4');
                let position = 0;
                PDF.addImage(
                    FILEURI,
                    'PNG',
                    0,
                    position,
                    fileWidth,
                    fileHeight
                );
                PDF.save(
                    'Report_' +
                        new Date().getDate() +
                        '-' +
                        new Date().getMonth() +
                        '-' +
                        new Date().getFullYear() +
                        '.pdf'
                );
            }
        );
    }
    //download option check
    download_report(value: string) {
        if (value == 'xls') {
            this.downloadName = 'Export To Excel';
            this.xls();
        } else {
            this.downloadName = 'Export To PDF';
            this.pdf();
        }
    }

    // settable data
    setTableData(data: any) {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
    }

    //onchange report option
    changeReportType(type: string) {
        if (type == 'genrateBydept') {
            this.show = false;
            this.graphsContainer = [];
            this.reportName = 'Department Wise Report';
            this.dataSource = new MatTableDataSource();
            this.graphsContainer.push(this.departmentWiseExpenseChart);
            this.isLoading = true;
            this.setAllDepartmentReport();

            this.headerName = [
                'Department Name',
                'Department Code',
                'Head Code',
                'Head Name',
                'Costing',
                'Status',
            ];
            this.displayedColumns = [
                'departmentName',
                'departmentCode',
                'headedBy',
                'headedByName',
                'costing',
                'active',
            ];
            this.isLoading = false;
        } else {
            this.show = true;
            this.isGraphLoaded = true;
            //   this.setIndividualDepartment();

            this.graphsContainer = [];
            let deptApiSubscription = this._getDepartmentList
                .getDepartmentData()
                .subscribe({
                    next: (data) => {
                        this.departmentDropdown = data;
                        this.setAllDepartmentWiseData(
                            this.departmentDropdown[0].departmentId,
                            this.departmentDropdown[0].departmentName
                        );
                        this.isGraphLoaded = false;
                    },
                    error: (err: HttpErrorResponse) => {
                        if (err.status != 401) {
                            this.router.navigate(['something-went-wrong']);
                        } else {
                            this.dialog.open(ErrorComponent, {
                                panelClass: 'modal',

                                width: '500px',
                                backdropClass: 'backdropBackground',
                                disableClose: true,
                            });
                        }
                        this.isGraphLoaded = false;
                    },
                    complete: () => {
                        console.log('complete');
                        deptApiSubscription.unsubscribe();
                    },
                });
            this.reportName = 'Individual Department';
        }
    }

    //table header set
    setTableHeader() {
        this.displayedColumns = [
            'employeeCode',
            'employeeName',
            'teamName',
            'roleName',
            'empType',
            'experience',
            'ctc',
        ];
        this.headerName = [
            'ECode',
            'Employee',
            'Team Name',
            'Role Name',
            'Emp Type',
            'Experience',
            'CTC',
        ];
    }

    //setting department wise data and  create chart
    setAllDepartmentWiseData(departmentId: string, departmentName: string) {
        /* Table Data Start */

        this.dataSource = new MatTableDataSource();
        this.isLoading = true;

        //table name
        this._reportAPIService.getTeamWiseCostChart(departmentId).subscribe({
            next: (r) => {
                this.displayedColumns = ['teamName', 'costing', 'active'];
                this.headerName = ['Team Name', 'Costing', 'Status'];
                this.setTableData(r);
            },
            error: (error: HttpErrorResponse) => {
                if (error.status != 401) {
                    this.router.navigate(['something-went-wrong']);
                } else {
                    this.dialog.open(ErrorComponent, {
                        panelClass: 'modal',

                        width: '500px',
                        backdropClass: 'backdropBackground',
                        disableClose: true,
                    });
                }
                this.isLoading = false;
            },
            complete: () => {
                this.isLoading = false;
            },
        });
        this.setTableHeader();

        /* Table Data End */

        this.graphsContainer = [];
        this.deptName = departmentName;

        //piechart experince wise chart api down
        let apiSubscriptionForExperinceWiseChart = this._reportAPIService
            .getExperienceWiseSalaryChart()
            .subscribe({
                next: (data: ExperinceWiseSalaryModel[]) => {
                    let modifiedData = data
                        .filter(
                            (exp) => exp.avgCtc != '0' && exp.experience != '0'
                        )
                        .map((exp) => ({
                            name: exp.experience + ' Yr',
                            y: +exp.avgCtc,
                        }));
                    let objToPass: ChartModel = {
                        title: 'Experience Wise Salary',
                        colors: this._colors,
                        innerData: modifiedData,
                    };
                    this.experienceWiseSalaryChart =
                        this._chartService.getPieChart(objToPass);
                },
                error: (error: HttpErrorResponse) => {
                    if (error.status == 401) {
                        this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',

                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                },
                complete: () => {
                    this.isGraphLoaded = false;
                    this.graphsContainer.push(this.experienceWiseSalaryChart);
                    apiSubscriptionForExperinceWiseChart.unsubscribe();
                },
            });
        //get team wise costings chart
        let apiSubscriptionForTeamWiseChart = this._reportAPIService
            .getTeamWiseCostChart(departmentId)
            .subscribe({
                next: (data: TeamWiseCosting[]) => {
                    let modifiedData = data.map((exp) => ({
                        name: exp.teamName.toUpperCase(),
                        y: +exp.costing,
                    }));
                    let objToPass: ChartModel = {
                        title: 'Team Wise Costing',
                        colors: this._colors,
                        innerData: modifiedData,
                    };
                    this.teamWiseCostingChart =
                        this._chartService.getPieChart(objToPass);
                },
                error: (error: HttpErrorResponse) => {
                    if (error.status != 401) {
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
                    this.isGraphLoaded = false;
                    this.graphsContainer.push(this.teamWiseCostingChart);
                    apiSubscriptionForTeamWiseChart.unsubscribe();
                },
            });
    }
    private _baseYear: number = 2010;

    createLineChart() {
        const apiSubscription = this._reportAPIService
            .getDepartmnetWiseCosting()
            .subscribe({
                next: (data: any[]) => {
                    let modifiedData: any = [];
                    //logic to modify data to set the line chart
                    data.forEach((element) => {
                        let name = element.departmentName;
                        let temp = element.costing;
                        let minimum = Math.min.apply(
                            null,
                            Object.keys(temp).map((item) => +item)
                        );
                        let year = [];
                        for (let i = this._baseYear; i < minimum; i++) {
                            i == minimum - 1 ? year.push(0) : year.push(null);
                        }
                        year.push(
                            ...Object.values(temp).map((item) => {
                                const str: string = item as unknown as string;
                                return parseInt(str);
                            })
                        );
                        modifiedData.push({
                            name: name,
                            data: year,
                        });
                    });
                    let objToPass: ChartModel = {
                        title: 'Department Wise Cost',
                        colors: this._colors,
                        innerDataForLineChart: modifiedData,
                    };
                    this.departmentWiseExpenseChart =
                        this._chartService.getLineChart(objToPass);
                },
                error: (error: HttpErrorResponse) => {
                    if (error.status != 401) {
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
                    this.isGraphLoaded = false;
                    this.graphsContainer.push(this.departmentWiseExpenseChart);
                    apiSubscription.unsubscribe();
                },
            });
    }
    setAllDepartmentReport() {
        let allDepartmentData: any;
        const apiSubscription = this._reportAPIService
            .getAllDepartmentReport()
            .subscribe({
                next: (data) => {
                    allDepartmentData = data;
                    this.setTableData(allDepartmentData);
                    this.isLoading = false;
                },
                error: (err: HttpErrorResponse) => {
                    if (err.status != 401) {
                        this.router.navigate(['something-went-wrong']);
                    } else {
                        this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',

                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                    this.isLoading = false;
                },
                complete: () => {
                    apiSubscription.unsubscribe();
                },
            });
    }
}

//needed in future
// setIndividualDepartment(pageNumber = 0, pageSize = 100) {
//     const apiSubscription = this._reportAPIService
//         .getIndividualDeparment(pageNumber, pageSize)
//         .subscribe({
//             next: (data) => {
//                 this.setTableData(data);
//             },
//             error: (err: HttpErrorResponse) => {
//                 if (err.status != 401) {
//                     this.router.navigate(['something-went-wrong']);
//                 } else {
//                     this.dialog.open(ErrorComponent, {
//                         panelClass: 'modal',

//                         width: '500px',
//                         backdropClass: 'backdropBackground',
//                         disableClose: true,
//                     });
//                 }
//             },
//             complete: () => {
//                 apiSubscription.unsubscribe();
//             },
//         });
// }
