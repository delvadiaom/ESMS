import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDepartmentComponent } from './add-department.component';
import { HttpClientModule } from '@angular/common/http';
import { MAT_DIALOG_DATA, MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { compileNgModule } from '@angular/compiler';

describe('AddDepartmentComponent', () => {
  let component: AddDepartmentComponent;
  let fixture: ComponentFixture<AddDepartmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddDepartmentComponent],
      imports: [HttpClientModule, MatDialogModule, BrowserAnimationsModule, ReactiveFormsModule],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialogRef, useValue: {} }
      ]

    })
      .compileComponents();

    fixture = TestBed.createComponent(AddDepartmentComponent);
    component = fixture.componentInstance;
    const dialogSpy = spyOn(TestBed.get(MatDialog), 'open')
    fixture.detectChanges();
  });

  it('Dialog created', () => {
    expect(component).toBeTruthy();
  });



  it("Testing FormGroup Element Count", () => {
    const formElements = fixture.debugElement.nativeElement.querySelector("#departmentForm");
    const inputElements = formElements.getElementsByTagName('input');
    expect(inputElements?.length).toEqual(4);
  })

  it("Check FormGroup initial value", () => {
    const formGroup = component.addDepartmentForm;
    const value = {
      departmentCode: '',
      departmentName: '',
      headedBy: '',
      skills: ''
    }
    expect(formGroup.value).toEqual(value)
  })

  it("Check departmentCode Validation", () => {

    const formElements = fixture.debugElement.nativeElement.querySelector("#departmentForm");
    const departmentCode: HTMLInputElement = formElements.getElementsByTagName('input')[0]
    console.log('departmentCode', departmentCode);

    departmentCode.value = "PDT001";
    departmentCode.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      const departmentCodeFromControl = component.addDepartmentForm.get('departmentCode');
      fixture.detectChanges();
      expect(departmentCode.value).toEqual(departmentCodeFromControl?.value);
      expect(departmentCodeFromControl?.errors).toBeNull();
    })
  })

  it("Check whole form Validation", () => {
    const formElements = fixture.debugElement.nativeElement.querySelector("#departmentForm");
    const inputElements: HTMLInputElement[] = formElements.getElementsByTagName('input');
    const dc = inputElements[0];
    const dn = inputElements[1];
    const hb = inputElements[2];
    const sk = inputElements[3];
    dc.value = "PDT001";
    dn.value = "UI";
    hb.value = "Asite700";
    sk.value = "Angular";
    dc.dispatchEvent(new Event('input'));
    dn.dispatchEvent(new Event('input'));
    hb.dispatchEvent(new Event('input'));
    sk.dispatchEvent(new Event('input'));
    const isValid = component.addDepartmentForm.valid;
    fixture.whenStable().then(() => {
      expect(isValid).toBeTruthy();
    })
  })
});
