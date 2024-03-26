import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ConfirmComponent } from 'src/app/Modules/shared/components/confirm/confirm.component';
import { AddTeamComponent } from './add-team/add-team.component';
import { Subscription } from 'rxjs';
import { TeamServiceService } from '../../services/team-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { DepartmentServiceService } from '../../services/department-service.service';
import { ErrorComponent } from 'src/app/Modules/shared/components/error/error.component';

export interface EmpData {
    no: number;
    code: string;
    name: string;
    department_name: string;
    team_name: string;
    role: string;
    status: string;
}

@Component({
    selector: 'app-team',
    templateUrl: './team.component.html',
    styleUrls: ['./team.component.scss'],
})
export class TeamComponent implements OnInit {
    displayedColumns: string[] = [
        'no',
        'code',
        'name',
        'department_name',
        'team_name',
        'role',
        'status',
        'log',
        'detail',
    ];
    dataSource!: MatTableDataSource<EmpData>;
    teamName: string;
    teamId: number;
    departmentId: number;
    isEmpdataLoading: boolean = false;
    _empDataApiSubscription!: Subscription;
    filter: boolean = false;
    isLoading: boolean = true;
    emps!: any[];
    departmentName!: string;

    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    constructor(
        private route: ActivatedRoute,
        private dialog: MatDialog,
        private departmentService: DepartmentServiceService,
        private teamService: TeamServiceService,
        private router:Router,
    ) {
        this.teamName = 'App Builder';
        this.teamId = 0;
        this.departmentId = 0;
    }

    editTeam(id: number) {
        this.dialog.open(AddTeamComponent, {
            panelClass: 'modal',
            data: {
                title: 'Edit Team',
                edit: true,
                teamId: id,
                departmentId: this.departmentId,
            },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
    }

    deleteTeam(id: number) {
        this.dialog.open(ConfirmComponent, {
            panelClass: 'modal',
            data: {
                title: 'Delete Team',
                teamName: this.teamName,
                teamId: id,
                departmentId: this.departmentId,
            },
            width: '800px',
            backdropClass: 'backdropBackground',
            disableClose: true,
        });
    }

    ngOnInit(): void {
        this.isLoading = true;
        this.route.params.subscribe((params: Params) => {
            this.teamId = +params['team'];
            this.departmentId = +params['id'];
            this.loadData();
        });

        this.teamService.fetchEmpsDataWithTeamId(this.teamId).subscribe( {


            next:(res:any)=>{
                this.emps = res;
                this.dataSource = new MatTableDataSource(this.emps);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;

            },
            error:(err:HttpErrorResponse)=>{
                if(err.status!=401){
                    this.router.navigate(['something-went-wrong'])
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
                this.isLoading = false;
            }
          //  console.log(data);
           
          
            
        })
    }

    loadData() {
        this.isEmpdataLoading = true;
        this.departmentService.departmentData.subscribe((res) => {
            res.forEach((element: any) => {
                if (element.departmentId === this.departmentId) {
                    this.departmentName = element.departmentName;
                }
            });
        });
        this._empDataApiSubscription = this.teamService
            .fetchEmpsDataWithTeamId(this.teamId)
            .subscribe({
                next: (response: EmpData[]) => {
                    console.log('-----fetching all employee details------');
                    this.dataSource = new MatTableDataSource(response);
                    setTimeout(() => {
                        this.dataSource.paginator = this.paginator;
                        this.dataSource.sort = this.sort;
                    }, 100);
                },
                error: (error: HttpErrorResponse) => {
                    this.isEmpdataLoading = false;
                    console.log(error);
                    if(error.status!=401){
                        this.router.navigate(['something-went-wrong']);
                    }else{
                        this.dialog.open(ErrorComponent, {
                            panelClass: 'modal',
                            
                            width: '500px',
                            backdropClass: 'backdropBackground',
                            disableClose: true,
                        });
                    }
                }, complete: () => {
                    this.isEmpdataLoading = false;
                    console.log('Success!!');
                },
            });
    }

    applyFilter(event: Event) {
        this.filter = true;
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataSource.filter = filterValue.trim().toLowerCase();

        if (this.dataSource.paginator) {
            this.dataSource.paginator.firstPage();
        }
    }
}
