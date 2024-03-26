
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { LoginResponse, LoginService } from 'src/app/services/login.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup | any;
    isLoginLoading: boolean = false;

    forgotForm: FormGroup | any;
    isForgotLoading: boolean = false;

    otpForm: FormGroup | any;
    isOtpLoading: boolean = false;

    passwordForm: FormGroup | any;
    isPasswordLoading: boolean = false;

    forgotEmail: string = "";
    userOtp: number = 0;

    errorMsg: string = "";

    responseObj: LoginResponse = {
        token: "string",
        userId: 0,
        empCode: "",
        email: "",
        priviledgeId: "",
        createdDate: "",
        modifiedDate: "",
        active: false
    }

    constructor(
        private loginService: LoginService,
        private _snackBar: MatSnackBar,
        private router: Router) { }

    ngOnInit(): void {
        this.loginService.userdata
        this.loginForm = new FormGroup({
            username: new FormControl("", [Validators.required]),
            password: new FormControl("", [Validators.required])
        })
        this.forgotForm = new FormGroup({
            email: new FormControl("", [Validators.required]),
        })
        this.otpForm = new FormGroup({
            otp: new FormControl("", [Validators.required]),
        })
        this.passwordForm = new FormGroup({
            newPassword: new FormControl("", [Validators.required]),
            confirmPassword: new FormControl("", [Validators.required]),
        })
    }

    formName: string = "login";


    setForm(formName: string) {
        this.formName = formName;
    }

    onSubmit(e: Event) {
        if(this.loginForm.valid)
        {
            e.preventDefault();
            this.isLoginLoading = true;
    
            const user = {
                username: this.loginForm.get('username').value,
                password: this.loginForm.get('password').value
            }
            this.loginService.login(user).subscribe({
                next: (response: any) => {
                    const token = Object.keys(response)[0]
                    this.responseObj.token = token;
                    this.responseObj.userId = response[token].userId;
                    this.responseObj.empCode = response[token].empCode;
                    this.responseObj.email = response[token].email;
                    this.responseObj.priviledgeId = response[token].priviledgeId;
                    this.responseObj.createdDate = response[token].createdDate;
                    this.responseObj.modifiedDate = response[token].modifiedDate;
                    this.responseObj.active = response[token].active;
    
                    sessionStorage.setItem('adminCode', this.responseObj.empCode);
                    sessionStorage.setItem('token', token);
                    console.log(this.responseObj);
                    this.isLoginLoading = false;
    
                    this.loginService.userdata.next(this.responseObj);
    
                }, error: (error: HttpErrorResponse) => {
    
                    console.log(error);
                    if(error.status == 404){
                        this._snackBar.open("Invalid Credentials!", "Close", {
                            duration: 3000,
                            panelClass: ['red-snackbar', 'login-snackbar'],
                        })
                    }else{
                        this._snackBar.open("Something went wrong!", "Close", {
                            duration: 3000,
                            panelClass: ['red-snackbar', 'login-snackbar'],
                        })
                    }
                    this.isLoginLoading = false;
                }, complete: () => {
                    this.loginService.userLogin = true;
                    this.router.navigate(["dashboard"]);
                }
            })
        }else{
            this._snackBar.open("Please fill form properly!", "Close", {
                duration: 3000,
                panelClass: ['red-snackbar', 'login-snackbar'],
            })
        }
    }

    onforgotSubmit(e: Event) {
        if (this.forgotForm.valid) {
            console.log(this.forgotForm.valid)
            this.isForgotLoading = true;
            e.preventDefault();
            this.loginService.forgotpassword(this.forgotForm.get('email').value).subscribe({
                next: (response: any) => {
                    this.forgotEmail = this.forgotForm.get('email').value;
                    this.isForgotLoading = false;
                    this.formName = 'otp';
                    this.errorMsg = "";
                }, error: (error: HttpErrorResponse) => {
                    if (error.status === 400) {
                        this.errorMsg = "Invalid Email"
                    } else {
                        this.errorMsg = "Something went wrong"
                    }
                    // console.log(error);
                    this.isForgotLoading = false;
                }, complete: () => {
                    console.log("Complete");
                }
            })
        }
    }

    onOtpSubmit(e: Event) {
        if (this.otpForm.valid) {
            this.isOtpLoading = true;
            e.preventDefault();
            // console.log(this.otpForm.get('otp').value);
            this.loginService.verifyOtp({
                mail: this.forgotEmail,
                otp: this.otpForm.get('otp').value
            }).subscribe({
                next: (response: any) => {
                    this.isOtpLoading = false;
                    this.userOtp = this.otpForm.get('otp').value;
                    this.formName = 'password';
                    this.errorMsg = "";
                }, error: (error: HttpErrorResponse) => {
                    if (error.status === 400) {
                        this.errorMsg = "Invalid OTP";
                    } else {
                        this.errorMsg = "Something went wrong";
                    }
                    this.isOtpLoading = false;
                }, complete: () => {
                    console.log("Complete");
                }
            })
        } else {
            alert("Not valid")
        }
    }

    onPasswordSubmit(e: Event) {
        if (this.passwordForm.valid && this.passwordForm.get('newPassword').value === this.passwordForm.get('confirmPassword').value) {
            this.isPasswordLoading = true;
            e.preventDefault();
            this.loginService.setNewPassword({
                mail: this.forgotEmail,
                otp: this.userOtp,
                password: this.passwordForm.get('newPassword').value
            }).subscribe({
                next: (response: any) => {
                    this.isPasswordLoading = false;
                    this.formName = 'login';
                }, error: (error: HttpErrorResponse) => {
                    if (error.status === 400) {
                        this.errorMsg = error.error.status;
                    } else {
                        this.errorMsg = "Something went wrong";
                    }
                    this.isPasswordLoading = false;
                }, complete: () => {
                    console.log("Complete");
                }
            })
        }
    }
}