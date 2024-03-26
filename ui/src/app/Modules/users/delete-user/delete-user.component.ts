import {
    animate,
    keyframes,
    style,
    transition,
    trigger,
} from '@angular/animations';
import { Component, Inject, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { UsersService } from '../services/users.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ErrorComponent } from '../../shared/components/error/error.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-delete-user',
    templateUrl: './delete-user.component.html',
    styleUrls: ['./delete-user.component.scss'],
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
export class DeleteUserComponent implements OnDestroy {
    private deleteUserSubscriber: Subscription | undefined;
    deleteBtnText: string = 'Delete';
    btnEnable = false;

    constructor(
        @Inject(MAT_DIALOG_DATA) public data: any,
        private userDataService: UsersService,
        private dialog: MatDialogRef<DeleteUserComponent>,
        private router: Router,
        private _snackBar:MatSnackBar,
        private dialogMat: MatDialog
    ) { }

    // Deleting user
    onDelete(data: any) {
        this.deleteBtnText = 'Deleting....'; // Changing button text
        this.btnEnable = true;
        let observable = this.userDataService.deleteUser(data);
        this.deleteUserSubscriber = observable.subscribe({
            next: (data) => { 
                this._snackBar.open("User deleted Successfully!", "Close", {
                    duration: 5000,
                    panelClass: ['green-snackbar', 'login-snackbar'],
                })
            },
            error: (error: HttpErrorResponse) => {
                if (error.status != 401) {
                    this.router.navigate(['something-went-wrong']);
                } else {
                    this.dialogMat.open(ErrorComponent, {
                        panelClass: 'modal',
                        width: '500px',
                        backdropClass: 'backdropBackground',
                        disableClose: true,
                    });
                }

                this.dialog.close();
            },
            complete: () => {
                console.log('DELETE COMPLETED!');
                this.dialog.close({ reload: true });
            },
        });
    }

    ngOnDestroy(): void {
        this.deleteUserSubscriber?.unsubscribe();
    }
}
