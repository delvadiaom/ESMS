import { HttpErrorResponse } from '@angular/common/http';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { roleData, RolesServiceService } from '../services/roles-service.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
    selector: 'app-role-component',
    templateUrl: './role-component.component.html',
    styleUrls: ['./role-component.component.scss']
})
export class RoleComponentComponent implements OnInit, OnDestroy {
    displayedColumns: string[] = ['priviledgeId', 'priviledgeName', 'action'];

    @ViewChild(MatSort) sort!: MatSort;

    isLoading = true;
    private _apiSubscription: Subscription | any;
    private _deleteApiSubscription: Subscription | any;
    dataSource!: MatTableDataSource<roleData>;

    constructor(private roleService: RolesServiceService, private _snackBar: MatSnackBar,private router: Router) {
    }


    ngOnInit(): void {
        this.loadData();
    }

    loadData() {
        this.isLoading = true;
        this._apiSubscription = this.roleService.getAllRoles()
            .subscribe({
                next: (response: any) => {
                    // console.log(response)
                    this.dataSource = new MatTableDataSource(response);
                    this.dataSource.sort = this.sort;
                }, error: (error: HttpErrorResponse) => {
                    this.isLoading = false;
                    console.log(error);
                    if (error.status != 401) {
                        this.router.navigate(['something-went-wrong']);
                    }
                }, complete: () => {
                    this.isLoading = false;
                    console.log("Success!!");
                }
            })
    }

    editPermission(priviledgeId: string) {
        this.router.navigate(['roles/edit/' + priviledgeId]);
    }


    deleteRole(priviledgeId: string) {
        this.isLoading = true;
        if (window.confirm("Are you sure ? ")) {
            this._deleteApiSubscription = this.roleService.deleteRoleDetails(priviledgeId).subscribe({
                next: (response: any) => {
                    console.log("Delete Done");
                    if (response.status === "Can't delete some users have this role") {
                        this._snackBar.open("Can't delete some users have this role!", "Close", {
                            duration: 5000,
                            panelClass: ['green-snackbar', 'login-snackbar'],
                        })
                    }else{
                        this._snackBar.open("Role deleted Successfully!", "Close", {
                            duration: 5000,
                            panelClass: ['green-snackbar', 'login-snackbar'],
                        })
                    }

                }, error: (error: HttpErrorResponse) => {
                    this.isLoading = false;
                    console.log(error);
                    if (error.status != 401) {
                        this.router.navigate(['something-went-wrong']);
                    }
                }, complete: () => {
                    this.isLoading = false;
                    this.loadData();
                    console.log("Success!!");
                }
            })
        }
    }

    applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    ngOnDestroy(): void {
        if (this._apiSubscription) {
            this._apiSubscription.unsubscribe();
        }
    }
}
