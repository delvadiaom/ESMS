import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApiFailedComponent } from './api-failed.component';

describe('ApiFailedComponent', () => {
  let component: ApiFailedComponent;
  let fixture: ComponentFixture<ApiFailedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApiFailedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApiFailedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
