import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../../../store';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { config } from '../../../../core/config/config';
import { ConfigService } from '../../../../core/config/config.service';

import { AdminCockpitService } from '../../../services/admin-cockpit.service';

import { CoreModule } from '../../../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../../../shared/common/test-utils';
import { userData } from '../../../../../in-memory-test-data/db-admin-data';
import { TranslocoService } from '@ngneat/transloco';
import { getTranslocoModule } from '../../../../transloco-testing.module';

import { DeleteDialogComponent } from './delete-dialog.component';

const adminCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(of(userData)),
  snackBar: jasmine.createSpy('snackBar').and.returnValue(of("Alle Felder müssen ausgefüllt sein", "verstanden")),
  sendPasswordResetMail: jasmine.createSpy('sendPasswordResetMail').and.returnValue(of(null)),
  deleteUser(){return null},
};

class TestBedSetUp {
  static loadAdminCockpitServiceStud(adminCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [DeleteDialogComponent],
      providers: [
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: AdminCockpitService, useValue:
          adminCockpitServiceStub
        },
        ConfigService,
        DeleteDialogComponent,
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

fdescribe('DeleteDialogComponent', () => {
  let component: DeleteDialogComponent;
  let fixture: ComponentFixture<DeleteDialogComponent>;
  let store: Store<State>;
  let initialState;
  let adminCockpitService: AdminCockpitService;
  // let adminDialogComponent: AdminDialogComponent;
  let configService: ConfigService;
  let el: DebugElement;
  let translocoService: TranslocoService;


  beforeEach(async(()  => {
    initialState = { config };
    TestBedSetUp.loadAdminCockpitServiceStud(adminCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(DeleteDialogComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        adminCockpitService = TestBed.inject(AdminCockpitService);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should call deleteUser on click of delete button', fakeAsync(() => {
    spyOn(adminCockpitService,'deleteUser');
    fixture.detectChanges();
    tick(1000);
    const clearFilter = fixture.debugElement.nativeElement.querySelector('#deleteButton');
    clearFilter.click();
    fixture.detectChanges();
    tick();
    expect(adminCockpitService.deleteUser).toHaveBeenCalled();
  }));

  it('should cancel deletion on click of cancel button', fakeAsync(() => {
    spyOn(adminCockpitService,'deleteUser');
    fixture.detectChanges();
    tick(1000);
    const clearFilter = fixture.debugElement.nativeElement.querySelector('#cancelButton');
    clearFilter.click();
    fixture.detectChanges();
    tick();
    expect(adminCockpitService.deleteUser).not.toHaveBeenCalled();
  }));

});




// describe('DeleteDialogComponent', () => {
//   let component: DeleteDialogComponent;
//   let fixture: ComponentFixture<DeleteDialogComponent>;
//
//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [ DeleteDialogComponent ]
//     })
//     .compileComponents();
//   }));
//
//   beforeEach(() => {
//     fixture = TestBed.createComponent(DeleteDialogComponent);
//     component = fixture.componentInstance;
//     fixture.detectChanges();
//   });
//
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
// });
