import { Component, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivityLogServiceService } from '../../services/activity-log-service.service';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-single-user-activity-log',
    templateUrl: './single-user-activity-log.component.html',
    styleUrls: ['./single-user-activity-log.component.scss'],
    // providers: [ActivityLogServiceService],
})
export class SingleUserActivityLogComponent {
    displayedColumns = [
        'adminCode',
        'adminName',
        'empName',
        'empCode',
        'newValue',
        'columnName',
        'timeStamp',
    ];
    headerName = [
        'UserID',
        'User',
        'Employee',
        'ECode',
        'New Value / Old Value',
        'Note',
        'TimeStamp',
    ];

    dataSource: MatTableDataSource<any> = new MatTableDataSource();
    isLoading: boolean = true;
    titleName:string='';
    getUpdateLogsByEmpCode: Subscription | any;
    getUpdateLogsByAdminCode: Subscription | any;

    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: string,
        private service: ActivityLogServiceService
    ) {}

    setTableData(data: any) {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
    }

    ngOnInit(): void {
        this.isLoading = true;
       
        let searchString: string[] = [];
        searchString = this.data.split(' ');
        this.titleName = searchString[0];
        if (searchString[1] == '1') {
            // Provide Single Employee Logs
            this.service.getUpdateLogsByEmpCode(searchString[0]).subscribe({
                next: (r) => {
                    this.setTableData(r);
                    this.isLoading = false;
                },
                error: (error) => {
                    this.isLoading = false;
                },
                complete: () => {},
            });
        } else {
             // Provide Single User Logs
            this.service.getUpdateLogsByAdminCode(searchString[0]).subscribe({
                next: (r) => {
                    this.setTableData(r);
                    this.isLoading = false;
                },
                error: (error) => {
                    this.isLoading = false;
                },
                complete: () => {},
            });
        }
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }
    ngOnDestroy() {
        if (this.getUpdateLogsByEmpCode) {
            this.getUpdateLogsByEmpCode.unsubscribe();
        }
        if (this.getUpdateLogsByAdminCode) {
            this.getUpdateLogsByAdminCode.unsubscribe();
        }
    }
}
