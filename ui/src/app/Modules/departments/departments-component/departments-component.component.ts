import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';
import { AddDepartmentComponent } from '../add-department/add-department.component';
import { DepartmentModel } from '../Model/department.model';
import { DepartmentServiceService } from '../services/department-service.service';
@Component({
    selector: 'app-departments-component',
    templateUrl: './departments-component.component.html',
    styleUrls: ['./departments-component.component.scss'],
})
export class DepartmentsComponentComponent implements OnInit, OnDestroy {
    private _apiSubscription: any;
    displayedColumns: string[] = [
        'no',
        'departmentName',
        'departmentCode',
        'skills',
        'headedBy',
        'headedByName',
        'costing',
        'active',
        'action',
    ];
    private _departmentData!: any;
    dataSource!: MatTableDataSource<DepartmentModel>;
    isLoading = false;
    isDataLoading = false;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        private dialog: MatDialog,
        private _departmentSevice: DepartmentServiceService,
        private router:Router
    ) { }


    ngOnInit() {
        this.isDataLoading = true;
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        this._apiSubscription = this._departmentSevice
            .getDepartmentData()
            .subscribe({

                next:(res:any)=>{
                    this._departmentData = res;
                    this._departmentSevice.departmentData.next(res);
                    this.dataSource = new MatTableDataSource(this._departmentData);
                    setTimeout(() => {
                        this.dataSource.paginator = this.paginator;
                        this.dataSource.sort = this.sort;
                    });
                },
                error:(err:HttpErrorResponse)=>{
                    if (err.status != 401) {
                        this.router.navigate(['something-went-wrong']);
                    }else{
                        this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',
                            
                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }

                },
                complete:()=>{
                    this.isLoading = false
                    this.isDataLoading = false;

                }
               
                
                
            });
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
    }

    addDepartment() {
        this.openDialog('Add Department');
    }

    openDialog(title: string) {
        const dialogRef = this.dialog.open(AddDepartmentComponent, {
            panelClass: 'modal',
            data: { title, edit: false },
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

    ngOnDestroy() {
        this._apiSubscription.unsubscribe();
    }
}
