import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../../store';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { config } from '../../../core/config/config';
import { ConfigService } from '../../../core/config/config.service';

import { AdminCockpitService } from '../../services/admin-cockpit.service';
import { AdminDialogComponent } from './admin-dialog.component';

import { CoreModule } from '../../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../../shared/common/test-utils';
import { userData } from '../../../../in-memory-test-data/db-admin-data';
import { TranslocoService } from '@ngneat/transloco';
import { getTranslocoModule } from '../../../transloco-testing.module';

const adminCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(of(userData)),
  snackBar: jasmine.createSpy('snackBar').and.returnValue(of("Alle Felder müssen ausgefüllt sein", "verstanden")),
  sendPasswordResetMail: jasmine.createSpy('sendPasswordResetMail').and.returnValue(of(null)),
};

const mockDialog = {
  open: jasmine.createSpy('dialog.open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const translocoServiceStub = {
  selectTranslateObject: of({
    close: 'Close',
  } as any),
};

class TestBedSetUp {
  static loadAdminCockpitServiceStud(adminCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [AdminDialogComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: MatDialog, useValue: mockDialog },
        { provide: AdminCockpitService, useValue:
          adminCockpitServiceStub
        },
        ConfigService,
        AdminDialogComponent,
        TranslocoService,
        provideMockStore({ initialState }),
      ],
      imports: [
        MatDialogModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
      ],
    });
  }
}

fdescribe('AdminDialogComponent', () => {
  let component: AdminDialogComponent;
  let fixture: ComponentFixture<AdminDialogComponent>;
  let store: Store<State>;
  let initialState;
  let adminCockpitService: AdminCockpitService;
  // let adminDialogComponent: AdminDialogComponent;
  let configService: ConfigService;
  let el: DebugElement;
  let translocoService: TranslocoService;
  let dialog: MatDialog;

  beforeEach(async(()  => {
    initialState = { config };
    TestBedSetUp.loadAdminCockpitServiceStud(adminCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AdminDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        adminCockpitService = TestBed.inject(AdminCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });


  it('should open deleteDialog on click of delete button', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick(1000);
    const clearFilter = fixture.debugElement.nativeElement.querySelector('#delete');
    clearFilter.click();
    fixture.detectChanges();
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));

  it('should open send reset mail on click of reset button', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick(1000);
    const clearFilter = fixture.debugElement.nativeElement.querySelector('#reset');
    clearFilter.click();
    fixture.detectChanges();
    tick();
    expect(adminCockpitService.sendPasswordResetMail).toHaveBeenCalled();
  }));

});


// fdescribe('AdminDialogComponent', () => {
//   let component: AdminDialogComponent;
//   let fixture: ComponentFixture<AdminDialogComponent>;
//
//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [ AdminDialogComponent ]
//     })
//     .compileComponents();
//   }));
//
//   beforeEach(() => {
//     fixture = TestBed.createComponent(AdminDialogComponent);
//     component = fixture.componentInstance;
//     fixture.detectChanges();
//   });
//
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
// });
