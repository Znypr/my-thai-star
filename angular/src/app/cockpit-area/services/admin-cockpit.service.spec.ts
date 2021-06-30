import { async, ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { config } from '../../core/config/config';
import { ConfigService } from '../../core/config/config.service';

import { AdminCockpitComponent } from '../admin-cockpit/admin-cockpit.component';
import { AdminCockpitService } from './admin-cockpit.service';

import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterModule, ActivatedRoute, ParamMap } from '@angular/router';
import { OverlayModule } from '@angular/cdk/overlay';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import {
  FilterAdminCockpit,
  Pageable,
  Sort,
} from 'app/shared/backend-models/interfaces';


const configServiceStub = {
  getRestPathRoot: jasmine
    .createSpy('getRestPathRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/')),
  getRestServiceRoot: jasmine
    .createSpy('getRestServiceRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/services/rest/')),
};

fdescribe('AdminCockpitService', () => {
  // let service: AdminCockpitService;
  let adminCockpitService: AdminCockpitService;
  let httpTestingController: HttpTestingController;
  let initialState;

  beforeEach(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({ initialState }),
        AdminCockpitService,
        MatSnackBar,
        { provide: ConfigService, useValue: configServiceStub },
      ],
      imports: [HttpClientTestingModule,BrowserAnimationsModule, RouterModule ,HttpClientModule,OverlayModule, RouterTestingModule],//.withRoutes([{path: 'admin', component: AdminCockpitComponent}])
    }).compileComponents();
    adminCockpitService = TestBed.inject(AdminCockpitService);
      httpTestingController = TestBed.inject(HttpTestingController);
  });


  // beforeEach(() => {
  //   TestBed.configureTestingModule({});
  //   service = TestBed.inject(AdminCockpitService);
  // });

  it('should be created', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
    expect(service).toBeTruthy();
  }));


  it('should send addUser-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.addUser("Tester","tester@mail.com",0,"password").subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'usermanagement/v1/user');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send getUsers-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.getUsers({  pageSize: 8, pageNumber: 0,}, [], new FilterAdminCockpit).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'usermanagement/v1/user/search');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send deleteUser-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.deleteUser(0).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'usermanagement/v1/user/0');
      expect(req.request.method).toEqual('DELETE');
      req.flush('Success');
  }));

  it('should send sendPasswordResetMail-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.sendPasswordResetMail(0).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'usermanagement/v1/resetPassword/0');
      expect(req.request.method).toEqual('GET');
      req.flush('Success');
  }));

  it('should send changePassword-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.changePassword(0,"password","RT_20210517_74da0febeb981971f39e7620d6e03eb5").subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'usermanagement/v1/changePassword');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));


});
