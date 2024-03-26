import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Component, Inject } from '@angular/core';
import {
    trigger,
    transition,
    keyframes,
    style,
    animate,
} from '@angular/animations';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TeamServiceService } from '../../../services/team-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ErrorComponent } from 'src/app/Modules/shared/components/error/error.component';

@Component({
    selector: 'app-add-team',
    templateUrl: './add-team.component.html',
    styleUrls: ['./add-team.component.scss'],
    animations: [
        trigger('rotate', [
            transition(':enter', [
                animate(
                    '600ms',
                    keyframes([
                        style({ transform: 'rotate(0deg)', offset: '0' }),
                        style({ transform: 'rotate(180deg)', offset: '1' }),
                    ])
                ),
            ]),
        ]),
    ],
})
export class AddTeamComponent {
    formLoading: boolean = false;
    deptId: number;
    isTeamReqProcessing = false;
    addTeamForm!: FormGroup;
    
    constructor(
        @Inject(MAT_DIALOG_DATA)
        public data: {
            title: string;
            edit: boolean;
            departmentId: number;
            teamId: number;
        },
        private teamSevice: TeamServiceService,
        private addTeamDialog: MatDialogRef<AddTeamComponent>,
        private router:Router,
        private dialog:MatDialog
        ) {
        // Extract department Id 
        this.deptId = this.data.departmentId;
    }
  
    ngOnInit(): void {
        this.loadData();
    }

    // Load Data
    loadData() {
        this.formLoading = true;

        if (this.data.edit) { // For Edit Team Form
            // Fetch Data
            this.teamSevice
                .getSingleTeamData(this.data.teamId)
                .subscribe({


                    next:(response:any)=>{
                        console.log(response);
                        this.addTeamForm = new FormGroup({
                            teamCode: new FormControl(
                                response.teamCode,
                                [Validators.required,Validators.maxLength(10),Validators.pattern(/^[a-zA-Z0-9_-]*$/)]
                            ),
                            teamName: new FormControl(
                                response.teamName,
                                [Validators.required,Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]
                            ),
                            leadBy: new FormControl(
                                response.leadBy,
                                [Validators.maxLength(20),Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]
                            ),
                        });
                        
                        
                    },
                    error:(error:HttpErrorResponse)=>{
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
                    },
                    complete:()=>{
                        this.formLoading = false;

                    }
                    
                   
                });
        } else { // For Add Team Form 
            this.addTeamForm = new FormGroup({
                teamCode: new FormControl("", [Validators.required,Validators.maxLength(10),Validators.pattern(/^[a-zA-Z0-9_-]*$/)]),
                teamName: new FormControl("", [Validators.required,Validators.maxLength(20),Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]),
                leadBy: new FormControl("", [Validators.maxLength(10),Validators.pattern(/^[a-zA-Z0-9\s_-]*$/)]),
            });

            this.formLoading = false;
        }
    }

    // Submit Data 
    submit_team(departmentId: number) {
        this.isTeamReqProcessing = true;
        
        if (!this.data.edit) { // Add Team  
            const teamData = this.addTeamForm.value;
            console.log(teamData);
            this.teamSevice.addTeam({ departmentId, ...teamData }).subscribe({
                next: (response: any) => {
                    console.log(response);
                },
                error: (error: HttpErrorResponse) => {
                    this.isTeamReqProcessing = false;
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
                    
                },
                complete: () => {
                    console.log('Team Added!!!');
                    this.isTeamReqProcessing = false;
                    this.addTeamDialog.close({ reload: true });
                },
            });
        } else { // Edit Team
            const teamData = this.addTeamForm.value;
            this.teamSevice
                .updateTeam({
                    teamId: this.data.teamId,
                    departmentId: this.data.departmentId,
                    ...teamData,
                })
                .subscribe({
                    next: (response: any) => {
                        console.log(response);
                    },
                    error: (error: HttpErrorResponse) => {
                        this.isTeamReqProcessing = false;
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
                    },
                    complete: () => {
                        this.isTeamReqProcessing = false;
                        console.log('Team Updated!!!');
                        this.addTeamDialog.close({ reload: true });
                    },
                });
        }
    }



}
