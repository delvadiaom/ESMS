import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { RolesServiceService, roleData } from '../services/roles-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { permissions } from '../services/roles-service.service';

@Component({
    selector: 'app-edit-role',
    templateUrl: './edit-role.component.html',
    styleUrls: ['./edit-role.component.scss']
})
export class EditRoleComponent {
    permissionForm: FormGroup;
    _apiSubscription: Subscription | any;
    _paramsSubscription: Subscription | any;
    isRoleEditProcessing: boolean = false;
    permissions = permissions;

    roleid: number;
    curRoleDetails: roleData = {
        priviledge: "",
        priviledgeName: ""
    };
    permissionArr: string[] = [];
    isFormdataFetching: boolean = false;

    formControlGenerator() {
        let formControlObj: any = {}
        formControlObj['roleName'] = this.fb.control("", Validators.required)
        this.permissions.forEach((val) => {
            let tempObj: any = {}
            val.nested.forEach((subVal) => {
                if(subVal[1])
                {
                    tempObj[`${val.shortForm}${(<string>subVal[2])}`] = this.fb.control({ value: subVal[1], disabled: true })
                }else{
                    tempObj[`${val.shortForm}${(<string>subVal[2])}`] = this.fb.control({ value: subVal[1], disabled: false })                    
                }
            })
            formControlObj[`${val.shortForm}`] = this.fb.group(tempObj);
        })
        return formControlObj
    }



    constructor(
        private fb: FormBuilder,
        private roleService: RolesServiceService,
        private router: Router,
        private activeRoute: ActivatedRoute
    ) {
        this.permissionForm = this.fb.group(this.formControlGenerator())
        this.roleid = 0;
    }

    loadData() {
        this.isFormdataFetching = true;
        this._apiSubscription = this.roleService.getSingleRole(this.roleid)
            .subscribe({
                next: (response: any) => {
                    this.curRoleDetails = response;
                    this.permissionArr = this.curRoleDetails.priviledge.split("#");

                    this.permissionForm.controls['roleName'].setValue(this.curRoleDetails.priviledgeName);

                    let formValueObj: any = [];
                    this.permissionArr.forEach((permission: string) => {
                        if (permission) {
                            formValueObj[permission] = true;
                            (<FormGroup>this.permissionForm.controls[permission[0]]).controls[permission]?.setValue(true);
                        }
                    });

                }, error: (error: HttpErrorResponse) => {
                    console.log(error);
                    if(error.status!=401){
                        this.router.navigate(['something-went-wrong']);
                    }
                }, complete: () => {
                    this.isFormdataFetching = false;
                    console.log("Success!!");
                }
            })
    }

    getParams() {
        this._paramsSubscription = this.activeRoute.params
            .subscribe(params => {
                this.roleid = params['roleid'];
            });
    }

    editRoleSubmit() {
        this.isRoleEditProcessing = true;
        let menuPermission = "";
        Object.keys(this.permissionForm.controls).forEach(key => {
            if (typeof this.permissionForm.controls[key].value === "object") {                   
                Object.keys((<FormGroup>this.permissionForm.controls[key]).controls).forEach(subKey => {
                    if ((<FormGroup>this.permissionForm.controls[key]).controls[subKey].value) {
                        menuPermission += (subKey + '#')
                    }
                })
            }
        });

        this._apiSubscription = this.roleService.updateRoleDetails({
            priviledgeName: this.permissionForm.controls['roleName'].value.toUpperCase(),
            priviledgeId: this.roleid,
            priviledge: menuPermission
        }).subscribe({
            next: (response: any) => {
                console.log(response);
                this.isRoleEditProcessing = false;
                this.router.navigate(["roles"])
            }, error: (error: HttpErrorResponse) => {
                console.log(error);
                this.isRoleEditProcessing = false;
                if(error.status!=401){
                    this.router.navigate(['something-went-wrong']);
                }
            }, complete: () => {
                console.log("Role Updated");
            }
        })
    }

    ngOnInit(): void {
        this.getParams()
        this.loadData();
    }

    ngOnDestroy(): void {
        if (this._apiSubscription) {
            this._apiSubscription.unsubscribe();
        }
        if (this._paramsSubscription) {
            this._paramsSubscription.unsubscribe();
        }
    }


}
