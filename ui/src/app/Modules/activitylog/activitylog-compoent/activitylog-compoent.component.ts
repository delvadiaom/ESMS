import { Component, ViewChild, ElementRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { SingleUserActivityLogComponent } from './single-user-activity-log/single-user-activity-log.component';
import { ActivityLogServiceService } from '../services/activity-log-service.service';
import { Subscription } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NumberFormatStyle } from '@angular/common';

@Component({
    selector: 'app-activitylog-compoent',
    templateUrl: './activitylog-compoent.component.html',
    styleUrls: ['./activitylog-compoent.component.scss'],
})
export class ActivitylogCompoentComponent {
    updateLogSubscribe: Subscription | any;
    addDeleteLogSubscribe: Subscription | any;

    dataSource: MatTableDataSource<any> = new MatTableDataSource();
    isLoading: boolean = true;
    refresh: boolean = true;

    displayedColumns1: string[] = [];
    headerName1: string[] = [];

    dataUD: any;
    dataID: any;

    currentLog: number = 1;
    typeName: string = 'Update Logs';
    logType = [
        { name: 'Update Logs', value: '1' },
        { name: 'Add/Delete Logs', value: '2' },
    ];

    updateLogCount: number = 0;
    addDeleteLogCount: number = 0;
    updatePage: number = 1;
    addDeletePage: number = 1;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        public dialog: MatDialog,
        private service: ActivityLogServiceService,
        private _snackBar: MatSnackBar
    ) {}

    ngOnInit() {
        this.isLoading = true;
        this.setHeaderAndRow();
        this.fetchUpdateLogCount();
        this.fetchUpdateLogByPage();
    }
    cupg: number = 1;
    totpg: number = 1;
    nonextpg: Boolean = true;
    noprevpg: Boolean = true;
    customPageChange(option: number) {
        this.isLoading = true;
        this.dataSource = new MatTableDataSource();
        if (this.currentLog == 1) {
            // Update
            this.totpg = this.updateLogCount;
            if (option == 1) {
                //Next Page
                this.noprevpg = true;
                this.cupg = ++this.updatePage;
                if (this.updatePage == this.totpg) {
                    this.cupg = this.updatePage;
                    this.nonextpg = false;
                }
            } else { // Previous Page
                this.nonextpg = true;
                this.cupg = --this.updatePage;
                if (this.updatePage <= 1) {
                    this.cupg = this.updatePage;
                    this.noprevpg = false;
                }
            }
            this.fetchUpdateLogByPage();
        } else {
            // Add/Delete
            this.totpg = this.addDeleteLogCount;
            if (option == 1) {
                this.noprevpg = true;
                this.cupg = ++this.addDeletePage;
                if (this.addDeletePage >= this.totpg) {
                    this.cupg = this.addDeletePage;
                    this.nonextpg = false;
                }
            } else { // Previous Page
                this.nonextpg = true;
                this.cupg = --this.addDeletePage;
                if (this.addDeletePage <= 1) {
                    this.cupg = this.addDeletePage;
                    this.noprevpg = false;
                }
            }
            this.addDeleteByPage();
        }
    }

    setTableData(data: any) {
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
    }

    setHeaderAndRow() {
        if (this.currentLog === 1) {
            this.displayedColumns1 = [
                'adminCode',
                'adminName',
                'empName',
                'empCode',
                'newValue',
                'columnName',
                'timeStamp',
                'viewlog',
            ];
            this.headerName1 = [
                'UserID',
                'User',
                'Employee',
                'ECode',
                // 'Action',
                'New Value / Old Value',
                'Description',
                'TimeStamp',
                'View Logs',
            ];
        } else {
            this.displayedColumns1 = [
                'adminCode',
                'adminName',
                'empName',
                'empCode',
                'action',
                'timeStamp',
                'viewlog',
            ];
            this.headerName1 = [
                'UserID',
                'User',
                'Employee',
                'ECode',
                'Action',
                'TimeStamp',
                'View Logs',
            ];
        }
    }

    fetchUpdateLogCount() {
        let getUpdateLogsCount = this.service.getUpdateLogsCount().subscribe({
            next: (res: number) => {
                this.updateLogCount = Math.ceil(res / 20);
                // this.updateLogCount = 5;
                this.totpg = this.updateLogCount;
            },
            error: (error) => {
                console.log(error);
            },
            complete: () => {
                getUpdateLogsCount.unsubscribe();
            },
        });
    }

    fetchUpdateLogByPage() {
        this.updateLogSubscribe = this.service
            .getUpdatedLogs(this.cupg)
            .subscribe({
                next: (r) => {
                    console.warn('Log From API ', r);
                    this.dataUD = r;
                    this.dataSource = new MatTableDataSource(r);
                    // setTimeout(() => {
                    //     this.paginator.pageIndex = this.updatePage;
                    //     this.paginator.length = this.updateLogCount;
                    //     this.dataSource.sort = this.sort;
                    // }, 100);
                    this.isLoading = false;
                },
                error: (error) => {
                    this.isLoading = false;
                    throw new Error('Error Occured During Fetching Data');
                },
                complete: () => {},
            });
    }

    fetchAddDeleteLogCount() {
        let getAddDeleteLogsCount = this.service
            .getAddDeleteLogsCount()
            .subscribe({
                next: (res: number) => {
                    this.addDeleteLogCount = Math.ceil(res / 20);
                    // this.addDeleteLogCount = 3;
                    this.totpg = this.addDeleteLogCount;
                },
                error: (error: any) => {
                    console.log(error);
                },
                complete: () => {
                    getAddDeleteLogsCount.unsubscribe();
                },
            });
    }

    addDeleteByPage() {
        this.addDeleteLogSubscribe = this.service
            .getAddDeleteLogs(this.cupg)
            .subscribe({
                next: (r) => {
                    this.dataID = r;
                    this.dataSource = new MatTableDataSource(r);
                    // setTimeout(() => {
                    //     this.paginator.pageIndex = this.addDeletePage;
                    //     this.paginator.length = this.addDeleteLogCount;
                    //     this.dataSource.sort = this.sort;
                    // }, 100);
                    this.isLoading = false;
                },
                error: (error) => {
                    this.isLoading = false;
                    throw new Error('Error Occured During Fetching Data');
                },
                complete: () => {},
            });
    }
    // Pagination
    //  totalRows = 0;
    //  pageSize = 20;
    //  currentPage = 0;
    //  pageSizeOptions: number[] = [20];
    //     pageChanged(event: PageEvent) {
    //         console.log({ event });
    //         this.dataSource = new MatTableDataSource();
    //         this.pageSize = event.pageSize;

    //         if (this.currentLog == 1) {
    //             this.isLoading = true;
    //             this.currentPage = this.updatePage;
    //             this.fetchUpdateLogByPage();
    //             this.updatePage++;
    //         } else {
    //             this.isLoading = true;
    //             this.currentPage = this.addDeletePage;
    //             this.addDeleteByPage();
    //             this.addDeletePage++;
    //         }
    //     }

    // Searching Functionality
    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
    }

    logTypeSelection(value: string) {
        if (value == '1') {
            this.currentLog = 1;
            this.cupg = this.updatePage;
            this.totpg = this.updateLogCount;
            this.typeName = 'Update Logs';
            this.dataSource = new MatTableDataSource();
            this.setHeaderAndRow();
            if (this.dataUD) {
                this.setTableData(this.dataUD);
            } else {
                this.isLoading = true;
                this.fetchUpdateLogCount();
                this.fetchUpdateLogByPage();
            }
        } else {
            this.currentLog = 2;
            this.cupg = this.addDeletePage;
            this.totpg = this.addDeleteLogCount;
            this.dataSource = new MatTableDataSource();
            this.typeName = 'Add/Delete Logs';
            this.setHeaderAndRow();

            if (this.refresh) {
                this.isLoading = true;
                this.fetchAddDeleteLogCount();
                this.addDeleteByPage();
                this.refresh = false;
            } else {
                this.setTableData(this.dataID);
            }
        }
    }

    /* Date Section Start */

    @ViewChild('sd') sd!: ElementRef<HTMLInputElement>;
    @ViewChild('ed') ed!: ElementRef<HTMLInputElement>;

    datefun() {
        if (
            this.sd.nativeElement.value != '' &&
            this.ed.nativeElement.value != '' &&
            this.sd.nativeElement.value < this.ed.nativeElement.value
        ) {
            this.dataSource = new MatTableDataSource();
            this.isLoading = true;

            this.service
                .getDurationLogs(
                    this.currentLog === 1 ? 'update' : 'insert',
                    this.sd.nativeElement.value,
                    this.ed.nativeElement.value
                )
                .subscribe({
                    next: (r) => {
                        this.setHeaderAndRow();
                        this.setTableData(r);
                        this.isLoading = false;
                    },
                    error: (error) => {
                        this.isLoading = false;
                        throw new Error('Error Occured While Fetching Data');
                    },
                    complete: () => {},
                });
        } else {
            this._snackBar.open('Please Select Proper Range!', 'Close', {
                duration: 3000,
                panelClass: ['green-snackbar', 'login-snackbar'],
            });
        }
    }

    // Dialog
    openDialog(code: string): void {
        this.dialog.open(SingleUserActivityLogComponent, {
            data: code,
            panelClass: 'modal',
            backdropClass: 'backdropBackground',
            width: '90vw',
            disableClose: true,
            maxHeight: '98vh',
        });
    }

    ngOnDestroy() {
        if (this.updateLogSubscribe) {
            this.updateLogSubscribe.unsubscribe();
        }
        if (this.addDeleteLogSubscribe) {
            this.addDeleteLogSubscribe.unsubscribe();
        }
    }
}
