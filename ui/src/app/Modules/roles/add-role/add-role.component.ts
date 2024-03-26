import { Component, ElementRef, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { RolesServiceService, permissions } from '../services/roles-service.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
    selector: 'app-add-role',
    templateUrl: './add-role.component.html',
    styleUrls: ['./add-role.component.scss']
})
export class AddRoleComponent implements OnInit, OnDestroy {
    permissionForm!: FormGroup;
    _apiSubscription: Subscription | any;
    isRoleCreationProcessing: boolean = false;
    permissions = permissions;

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
    ) {
    }

    ngOnInit(): void {
        this.permissionForm = this.fb.group(this.formControlGenerator())
    }

    addNewRoleSubmit() {
        if (this.permissionForm.valid) {
            this.isRoleCreationProcessing = true;
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

            console.log(menuPermission)

            this._apiSubscription = this.roleService.addNewRole({
                priviledgeName: this.permissionForm.controls['roleName'].value.toUpperCase(),
                priviledge: menuPermission
            }).subscribe({
                next: (response: any) => {
                    console.log(response);
                    this.isRoleCreationProcessing = false;
                    this.router.navigate(["roles"])
                }, error: (error: HttpErrorResponse) => {
                    console.log(error);
                    this.isRoleCreationProcessing = false;
                    if(error.status!=401){
                        this.router.navigate(['something-went-wrong']);
                    }
                }, complete: () => {
                    console.log("Role Added");
                }
            })
        } else {
            alert("Please fill form Properly");
        }
    }

    ngOnDestroy(): void {
        if (this._apiSubscription) {
            this._apiSubscription.unsubscribe();
        }
    }
}
