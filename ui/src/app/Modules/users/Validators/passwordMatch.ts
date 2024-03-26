
import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";



export  function passwordMatch(form:AbstractControl):null|ValidationErrors{

    if(form.get("empPassword")?.value!==form.get("confirmPassword")?.value){
        return {passwordMatchErr:true}
    }
    return null;

}




