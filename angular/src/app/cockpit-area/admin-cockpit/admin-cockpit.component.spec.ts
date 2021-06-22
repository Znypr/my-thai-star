import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { MatDialog, MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { config } from '../../core/config/config';
import { ConfigService } from '../../core/config/config.service';

import { AdminCockpitComponent } from './admin-cockpit.component';
import { AdminDialogComponent } from './admin-dialog/admin-dialog.component';
import { AdminCockpitService } from '../services/admin-cockpit.service';


import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { userData } from '../../../in-memory-test-data/db-admin-data';

const adminCockpitServiceStub = {
  getUsers: jasmine.createSpy('getUsers').and.returnValue(of(userData

  )),
  snackBar: jasmine.createSpy('snackBar').and.returnValue(of("Alle Felder müssen ausgefüllt sein", "verstanden")),
};
const mockDialog = {
  open: jasmine.createSpy('dialog.open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

class TestBedSetUp {
  static loadAdminCockpitServiceStud(adminCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [AdminCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: AdminCockpitService, useValue:
          adminCockpitServiceStub
        },
        ConfigService,
        AdminDialogComponent,
        provideMockStore({ initialState }),
      ],
      imports: [
        MatDialogModule,
        BrowserAnimationsModule,
        ReactiveFormsModule,
        CoreModule,
      ],
    });
  }
}

fdescribe('AdminCockpitComponent', () => {
  let component: AdminCockpitComponent;
  let fixture: ComponentFixture<AdminCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let adminCockpitService: AdminCockpitService;
  // let adminDialogComponent: AdminDialogComponent;
  let dialog: MatDialog;
  let configService: ConfigService;
  let el: DebugElement;

//   beforeEach(async(() => {
//     TestBed.configureTestingModule({
//       declarations: [ AdminCockpitComponent ]
//     })
//     .compileComponents();
//   }));
//   fixture = TestBed.createComponent(AdminCockpitComponent);
//   component = fixture.componentInstance;
//   fixture.detectChanges();
// });

  beforeEach(async(()  => {
    initialState = { config };
    TestBedSetUp.loadAdminCockpitServiceStud(adminCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(AdminCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        adminCockpitService = TestBed.inject(AdminCockpitService);
        dialog = TestBed.inject(MatDialog);
      });
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //C1 + C2
  it('should request adding a user', fakeAsync(() => {
    fixture.detectChanges();
    spyOn(component, 'getUserInput');
    expect(component.getUserInput).toHaveBeenCalledTimes(0);
    const username = el.query(By.css('#Username'));
    const email = el.query(By.css('#Email'));
    const role = el.query(By.css('#Role'));
    const password = el.query(By.css('#Password'));
    const submit = el.query(By.css('#submitButton'));

    username.nativeElement.value="tester";
    email.nativeElement.value="tester@mail.com";
    role.nativeElement.value=0;
    password.nativeElement.value="password";
    submit.nativeElement.click();
    fixture.detectChanges();
    tick();
    expect(component.getUserInput).toHaveBeenCalledTimes(1);

    //TODO add same user twice
  }));

  //C3 doesn't work
  it('unsupported email should show error message', async() => {
    fixture.detectChanges();

    const username = el.query(By.css('#Username'));
    const email = el.query(By.css('#Email'));

    email.nativeElement.value="test@test";
    username.nativeElement.click();
    fixture.detectChanges();
    await fixture.whenStable();

    // expect(fixture.debugElement.queryAll(By.css("#EmailError")).length).toEqual(1);
    expect(fixture.debugElement.queryAll(By.css("#EmailError"))[0].nativeElement.innerHTML.trim()).toMatch("Please enter a valid email address");

  });

  //C4 works
  it('number of roles should be 4', async() => {
    const trigger = fixture.debugElement.query(By.css('#Role')).nativeElement;
    click(trigger);
    fixture.detectChanges();
    await fixture.whenStable().then(() => {
       const options = fixture.debugElement.queryAll(By.css('option'));
       expect(options.length).toEqual(4);
    });
  });

//C5
  it('should create user "Tester" and show on table', async() => {

    fixture.detectChanges();
    spyOn(component, 'getUserInput');
    expect(component.getUserInput).toHaveBeenCalledTimes(0);
    const username = el.query(By.css('#Username'));
    const email = el.query(By.css('#Email'));
    const role = el.query(By.css('#Role'));
    const password = el.query(By.css('#Password'));
    const submit = el.query(By.css('#submitButton'));

    username.nativeElement.value="Tester";
    email.nativeElement.value="tester@mail.com";
    role.nativeElement.value=0;
    password.nativeElement.value="test";
    submit.nativeElement.click();
    fixture.detectChanges();
    await fixture.whenStable().then(() => {
    //   expect(component.users[component.users.length].user.username).toBe("Tester");
    // let newUser:any;
    //   console.log("Component: ",adminCockpitServiceStub);
    //   expect(component.users.length).toEqual(1);
      expect(component.getUserInput).toHaveBeenCalledTimes(1);
    });

  });

//C7
it('should create user with symbols', () => {
  fixture.detectChanges();
  const username = el.query(By.css('#Username'));
  const email = el.query(By.css('#Email'));
  const role = el.query(By.css('#Role'));
  const password = el.query(By.css('#Password'));
  const submit = el.query(By.css('#submitButton'));

  username.nativeElement.value="Jürgen";
  email.nativeElement.value="jürgen@mail.com";
  role.nativeElement.value=0;
  password.nativeElement.value="password";
  click(submit);
  fixture.detectChanges();
  expect(component).toBeTruthy();
});

//C8 TODO doesn't work yet
it('should open snackBar if not all fields are provided', async() => {
  fixture.detectChanges();
  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(0);
  const username = el.query(By.css('#Username'));
  const email = el.query(By.css('#Email'));
  const role = el.query(By.css('#Role'));
  const password = el.query(By.css('#Password'));
  const submit = el.query(By.css('#submitButton'));

  username.nativeElement.value="Jürgen";
  click(submit);
  fixture.detectChanges();
  await fixture.whenStable();

  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(1);
});
//test
fit('should create a snackbar using our component when the button is clicked', () => {
    const buttonDe: DebugElement = fixture.debugElement;
    const buttonEl: HTMLElement = buttonDe.nativeElement;
    const button = buttonEl.querySelector(
      'button#compButton'
    ) as HTMLButtonElement;
    button.click();
    fixture.detectChanges();
    const snackingDiv = document.querySelector('snack-bar-container');
    expect(snackingDiv).toBeTruthy();
  });

//C9
it('should show in paginator number of users', async() => {
  fixture.detectChanges();
  const numberOfUsers = el.query(By.css('mat-paginator'));
  expect(component).toBeTruthy();
});

//c10 in userarea service

//c12 in userarea service

//c14
it('should open dialog and close after clicking the close button', async() => {
  fixture.detectChanges();
  const clearFilter = el.queryAll(By.css('User'));
  click(clearFilter[0]);
  await fixture.whenStable();
  expect(dialog.open).toHaveBeenCalled();
  // expect(dialog.open).toHaveBeenCalled();
  // const closeButton = el.queryAll(By.css('#closeButton'));
  // click(closeButton[0]);
  // await fixture.whenStable();
  // fixture.detectChanges();
  // expect(dialog.open).toBeTruthy();
});

//C16
it('should show a snackBar after user has been deleted', async() => {
  fixture.detectChanges();
  spyOn(adminCockpitService, 'snackBar');
  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(0);
  const clearFilter = el.queryAll(By.css('.mat-row'));
  click(clearFilter[0]);
  await fixture.whenStable();
  expect(dialog.open).toHaveBeenCalled();
  const deleteButton = el.queryAll(By.css('#deleteButton'));
  click(deleteButton[0]);
  await fixture.whenStable();
  fixture.detectChanges();
  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(1);
});

//C19 in admin-cockpit.service

//C21 in userarea

//C22
it('should show a snackBar after user has received a reset link', async() => {
  fixture.detectChanges();
  spyOn(adminCockpitService, 'snackBar');
  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(0);
  const clearFilter = el.queryAll(By.css('.mat-row'));
  click(clearFilter[0]);
  await fixture.whenStable();
  expect(dialog.open).toHaveBeenCalled();
  const resetButton = el.queryAll(By.css('#resetButton'));
  click(resetButton[0]);
  await fixture.whenStable();
  fixture.detectChanges();
  expect(adminCockpitService.snackBar).toHaveBeenCalledTimes(1);
});



  //Test C1
  // it('should open OrderDialogComponent dialog on click of row', fakeAsync(() => {
  //   fixture.detectChanges();
  //   const clearFilter = el.queryAll(By.css('.mat-row'));
  //   click(clearFilter[0]);
  //   tick();
  //   expect(dialog.open).toHaveBeenCalled();
  // }));


});
