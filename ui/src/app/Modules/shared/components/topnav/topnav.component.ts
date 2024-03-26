import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditProfileComponent } from '../edit-profile/edit-profile.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-topnav',
    templateUrl: './topnav.component.html',
    styleUrls: ['./topnav.component.scss'],
})
export class TopnavComponent {
    constructor(private dialog: MatDialog, private _snackBar: MatSnackBar) { }

    openDialog(): void {
        const dialogRef = this.dialog.open(EditProfileComponent, {
            panelClass: 'modal',
            width: '800px',
            backdropClass: 'backdropBackground',
            maxHeight: '98vh',
        });
    }

    durationInSeconds = 5;

    openSnackBar(message: string) {
        this._snackBar.open("hello", "&times;", {
            duration: 3000,
            panelClass: ['red-snackbar', 'login-snackbar'],
        })
    }

}
