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


describe('AdminCockpitService', () => {
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
        ConfigService,
      ],
      imports: [HttpClientTestingModule, RouterModule ,HttpClientModule,OverlayModule, RouterTestingModule],//.withRoutes([{path: 'admin', component: AdminCockpitComponent}])
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
      adminCockpitService.addUser("Tester","tester@mail.com",0,"password").subscribe((response: HttpResponse<any>) => {
        expect(response.status).toBe(200);
        expect(response.body).toBe('Success');
      });
  }));

  it('should send deleteUser-request to server', inject(
    [AdminCockpitService], (service: AdminCockpitService) => {
      adminCockpitService.deleteUser(0).subscribe((response: HttpResponse<any>) => {
        expect(response.status).toBe(200);
        expect(response.body).toBe('Success');
      });
  }));

});
