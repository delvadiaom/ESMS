import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login.service';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class LoginGuard implements CanActivate {
    constructor(private loginService: LoginService, private router: Router) { }
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        const token = sessionStorage.getItem('token');
        if (!token) {
            this.router.navigate(["login"])
            return false;
        }
        this.loginService.validateToken(token).subscribe({
            next: (res: any) => {
                this.loginService.userdata.next(res);
            },
            error: (error: HttpErrorResponse) => {
                console.log(error);
                sessionStorage.removeItem('token');
                this.router.navigate(['login']);
                return false;
            },
            complete: () => {
                return true;
            }
        })
        return true;
    }
}
