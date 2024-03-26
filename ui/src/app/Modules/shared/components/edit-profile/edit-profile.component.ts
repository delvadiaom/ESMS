import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { LoginService } from 'src/app/services/login.service';
import { ProfileService } from '../../Services/profile.service';
import { image } from 'html2canvas/dist/types/css/types/image';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ErrorComponent } from '../error/error.component';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss'],
  animations: [trigger('rotate', [transition(':enter', [animate('600ms', keyframes([style({ transform: 'rotate(0deg)', offset: '0' }), style({ transform: 'rotate(180deg)', offset: '1' })]))])]),]
})

export class EditProfileComponent {
  imgUrl: any = 'https://cdn-icons-png.flaticon.com/512/149/149071.png';
  editOptions: boolean = false;
  profileForm: FormGroup;
  isFetched: boolean = true;
  isLoading: boolean = false;
  isReqProcessing: boolean = false;
  constructor(private loginService: LoginService,
    private profileService: ProfileService,
    private dialogMat: MatDialog,
    private _snackBar: MatSnackBar,
    private router: Router,
    private dialog: MatDialogRef<EditProfileComponent>) {
    this.profileForm = new FormGroup({
      name: new FormControl({ value: '', disabled: true }, Validators.required),
      email: new FormControl({ value: '', disabled: true }, Validators.required),
      gender: new FormControl({ value: '', disabled: true }, Validators.required),
      reportingManager: new FormControl({ value: '', disabled: true }, Validators.required),
      image: new FormControl({ value: '', disabled: true }, Validators.required),
      editable: new FormGroup({
        currentPassword: new FormControl('', Validators.required),
        password: new FormControl('', [Validators.required, Validators.pattern('(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&].{8,}')]),
        confirmPassword: new FormControl('', Validators.required),
      })
    },
      { validators: this.passwordMatch });
    this.profileForm.get('editable')?.disable()
  }
  ngOnInit(): void {

    this.loginService.userdata.subscribe((res) => {
      this.loadData(res.email)
    });
  }
  loadData(email: string) {
    this.isLoading = true;
    this.profileService.fetchUserData(email).subscribe(
      {
        next: (response) => {
          this.profileForm.patchValue({ email, name: response.fullName, gender: (response.gender === 'M' ? 'Male' : 'Female'), reportingManager: response.reportingManager, })
          this.profileService.getImg(response.empId).subscribe((res) => {
            this.imgUrl = URL.createObjectURL(res)
            this.isLoading = false;
          })
        }, 
        error: (err: HttpErrorResponse) => {
          this.isLoading = false; 
          this.isFetched = false;
          if (err.status != 401) {
            this.router.navigate(['something-went-wrong']);
          } else {
            this.dialogMat.open(ErrorComponent, {
              panelClass: 'modal',

              width: '500px',
              backdropClass: 'backdropBackground',
              disableClose: true,
            });
          }
        },
        complete: () => {
          this.isLoading = false; 
          this.isFetched = true
        }
      })
  }
  editProfile() { this.editOptions = true; this.profileForm.get('editable')?.enable(); }
  updateProfile() {
    // console.log(this.profileForm);
    this.isReqProcessing = true;
    this.profileService.setPassword(this.profileForm.get('email')?.value,
      this.profileForm.get("editable")?.get('currentPassword')?.value,
      this.profileForm.get("editable")?.get('password')?.value).subscribe({
        next: (res) => {
          console.log("res", res)
        },
        error: (err) => {
          this.isReqProcessing = false;
          if (err.status != 401) {
            this._snackBar.open("Please enter proper credentials", "Close", {
              duration: 3000,
              panelClass: ['green-snackbar', 'login-snackbar'],
            })
          } else {
            this.dialogMat.open(ErrorComponent, {
              panelClass: 'modal',
              width: '500px',
              backdropClass: 'backdropBackground',
              disableClose: true,
            });
          }
        },
        complete: () => {
          this.dialog.close()
          this.isReqProcessing = false;
          console.log('Password Changed!!');
        }
      })
  }
  back() { this.editOptions = false; this.profileForm.get('editable')?.disable(); }
  passwordMatch(form: AbstractControl): null | ValidationErrors {
    if (form?.get("editable")?.get('password')?.value !== form?.get("editable")?.get('confirmPassword')?.value) { return { passwordMatchErr: true } } return null;
  }
}