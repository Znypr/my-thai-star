import { async, ComponentFixture, TestBed } from '@angular/core/testing';
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




describe('ResetPasswordCockpitComponent', () => {
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
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        // AdminCockpitModule,
        CoreModule,
        RouterTestingModule
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
  it('should open a snackBar if passwords are not identical', async() => {
    spyOn(adminCockpitService, 'snackBar');
    const passwordField = el.query(By.css('#Password'));
    const paswordConfirmField = el.query(By.css('#confirmPassword'));
    const button = el.query(By.css('#submitButton'));

    passwordField.nativeElement.value="password";
    paswordConfirmField.nativeElement.value="password2";
    button.nativeElement.click();
    await fixture.whenStable();
    expect(adminCockpitService.snackBar).toHaveBeenCalled();
  });

  //C26 + C27
  it('should open a snackBar if passwords are not identical', async() => {
    spyOn(adminCockpitService, 'snackBar');
    const passwordField = el.query(By.css('#Password'));
    const paswordConfirmField = el.query(By.css('#confirmPassword'));
    const button = el.query(By.css('#submitButton'));

    passwordField.nativeElement.value="password";
    paswordConfirmField.nativeElement.value="password";
    button.nativeElement.click();
    await fixture.whenStable();
    expect(adminCockpitService.snackBar).toHaveBeenCalled();
  });
});
