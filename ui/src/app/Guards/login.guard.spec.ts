import { TestBed } from '@angular/core/testing';

import { LoginGuard } from './login.guard';
import { HttpClientModule } from '@angular/common/http';
import { LoginService } from '../services/login.service';
import { ReactiveFormsModule } from '@angular/forms';

describe('LoginGuard', () => {
  let guard: LoginGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientModule,ReactiveFormsModule],
        providers: [LoginService]
    });
    guard = TestBed.inject(LoginGuard);
    
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
