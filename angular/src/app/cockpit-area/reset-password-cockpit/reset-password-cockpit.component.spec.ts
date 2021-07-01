import { async, fakeAsync, tick, ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from '../../core/core.module';

import { ConfigService } from '../../core/config/config.service';
import { provideMockStore } from '@ngrx/store/testing';
import { config } from '../../core/config/config';

import { ResetPasswordCockpitComponent } from './reset-password-cockpit.component';
import { AdminCockpitService } from '../services/admin-cockpit.service';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs/internal/observable/of';
import { TranslocoService } from '@ngneat/transloco';
import { getTranslocoModule } from '../../transloco-testing.module';




fdescribe('ResetPasswordCockpitComponent', () => {
  let component: ResetPasswordCockpitComponent;
  let fixture: ComponentFixture<ResetPasswordCockpitComponent>;
  let adminCockpitService: AdminCockpitService;
  let initialState;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        // { provide: MAT_DIALOG_DATA, useValue: dialogOrderDetails },
        ConfigService,
        AdminCockpitService,
        TranslocoService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        // AdminCockpitModule,
        CoreModule,
        RouterTestingModule,
        getTranslocoModule(),
      ],
      declarations: [ ResetPasswordCockpitComponent ]
    })
    .compileComponents().then(() => {
      fixture = TestBed.createComponent(ResetPasswordCockpitComponent);
      component = fixture.componentInstance;
      el = fixture.debugElement;
      fixture.detectChanges();
    });
    adminCockpitService = TestBed.inject(AdminCockpitService);
  }));

  // beforeEach(() => {
  //   initialState = { config };
  //   fixture = TestBed.createComponent(ResetPasswordCockpitComponent);
  //   component = fixture.componentInstance;
  //   el = fixture.debugElement;
  //   configService = new ConfigService(store);
  //   adminCockpitService = TestBed.inject(AdminCockpitService);
  //   fixture.detectChanges();
  //
  // });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //C24
  it('should open a snackBar if passwords are not identical', fakeAsync(() => {
    fixture.detectChanges();
    tick();
    spyOn(adminCockpitService, 'snackBar');
    const passwordField = fixture.debugElement.nativeElement.querySelector('#Password');
    const passwordConfirmField = fixture.debugElement.nativeElement.querySelector('#confirmPassword');

// const password = fixture.debugElement.nativeElement.querySelector('#Password');
// const submit = fixture.debugElement.nativeElement.querySelector('#submitButton');


    const button = fixture.debugElement.nativeElement.querySelector('#submitButton');


    passwordField.value="password";
    passwordField.dispatchEvent(new Event('input'));
    passwordConfirmField.value="password2";
    passwordConfirmField.dispatchEvent(new Event('input'));
    button.click();
    fixture.detectChanges();
    tick();
    expect(adminCockpitService.snackBar).toHaveBeenCalled();
  }));

  // C26 + C27
  it('should open a snackBar if passwords are not identical', fakeAsync(() => {
    spyOn(adminCockpitService, 'changePassword').and.returnValue(of(null));
    fixture.detectChanges();
    tick();
    spyOn(adminCockpitService, 'snackBar');
    const passwordField = fixture.debugElement.nativeElement.querySelector('#Password');
    const passwordConfirmField = fixture.debugElement.nativeElement.querySelector('#confirmPassword');
    const button = fixture.debugElement.nativeElement.querySelector('#submitButton');

    passwordField.value="password";
    passwordField.dispatchEvent(new Event('input'));
    passwordConfirmField.value="password";
    passwordConfirmField.dispatchEvent(new Event('input'));
    button.click();
    fixture.detectChanges();
    tick();
    expect(adminCockpitService.snackBar).toHaveBeenCalled();
  }));
});
