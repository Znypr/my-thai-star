import { MatDialog,MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderCockpitComponent } from './order-cockpit.component';
import { config } from '../../core/config/config';
import {
  TestBed,
  ComponentFixture,
  async,
  fakeAsync,
  flush,
  tick
} from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslocoService } from '@ngneat/transloco';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../transloco-testing.module';
import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { ascSortOrder } from '../../../in-memory-test-data/db-order-asc-sort';
import { orderData } from '../../../in-memory-test-data/db-order';

const mockDialog = {
  open: jasmine.createSpy('dialog.open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const translocoServiceStub = {
  selectTranslateObject: of({
    reservationDateH: 'Reservation Date',
    emailH: 'Email',
    bookingTokenH: 'Reference Number',
    orderStatusH: 'Status',
    ownerH: 'Owner',
    tableH: 'Table',
    creationDateH: 'Creation date',
  } as any),
};

const waiterCockpitServiceStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(orderData)),
};

const waiterCockpitServiceSortStub = {
  getOrders: jasmine.createSpy('getOrders').and.returnValue(of(ascSortOrder)),
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [OrderCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: MAT_DIALOG_DATA, useValue: {} },
        { provide: WaiterCockpitService, useValue: waiterCockpitStub },
        TranslocoService,
        ConfigService,
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

fdescribe('OrderCockpitComponent', () => {
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;
  let overlay: HTMLElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  xit('should create component and verify content and total records of orders', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick();
    expect(component).toBeTruthy();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
    tick();
  }));

  xit('should go to next page of orders', () => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  });

  xit('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.orderClearFilters'));
    click(clearFilter);
    fixture.detectChanges();
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  }));

  xit('should open OrderDialogComponent dialog on click of row', fakeAsync(() => {
    fixture.detectChanges();
    const clearFilter = el.queryAll(By.css('#Booking'));
    click(clearFilter[0]);
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));

  xit('should filter the order table on click of submit', fakeAsync(() => {
    fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  }));

  it('should filter the order table on click of submit', async() => {
    fixture.detectChanges();
    const submit = el.query(By.css('.orderApplyFilters'));
    click(submit);
    // tick();
    expect(component.orders).toEqual(orderData.content);
    expect(component.totalOrders).toBe(8);
  });

  // //C47
  // fit('should change status preparing in cancelled', fakeAsync(() => {
  //   fixture.detectChanges();
  //   const clearFilter = el.queryAll(By.css('#optionForStatus'));
  //   click(clearFilter[0]);
  //   tick();
  //   expect(dialog.open).toHaveBeenCalled();
  // }));

  // //C50
  // it('should change status open in prepairing', async() => {
  //   fixture.detectChanges();
  //   spyOn(component, 'onChange');
  //   const row = el.query(By.css('#selectStatus')).nativeElement;
  //   row.click();
  //   fixture.detectChanges();
  //   const selectOptions = el.queryAll(By.css('#optionForStatus'));
  //   selectOptions[1].nativeElement.click();
  //   console.log("options: ",selectOptions.length);
  //   fixture.detectChanges();
  //   await fixture.whenStable();
  //   expect(component.onChange).toHaveBeenCalled();
  // });

  //C50
  it('should change status open in prepairing', async() => {
    fixture.detectChanges();
    spyOn(component, 'onChange');
    const row = el.query(By.css('#selectStatus')).nativeElement;
    row.click();
    fixture.detectChanges();
    const selectOptions = el.queryAll(By.css('#optionForStatus'));
    selectOptions[1].nativeElement.click();
    console.log("options: ",selectOptions[1]);
    fixture.detectChanges();
    expect(component.onChange).toHaveBeenCalled();
  });

  // //C50
  // fit('should change status open in prepairing', async() => {
  //   fixture.detectChanges();
  //   spyOn(component, 'onChange');
  //   const row = el.query(By.css('#selectStatus')).nativeElement;
  //   row.click();
  //   fixture.detectChanges();
  //   const selectOptions = el.queryAll(By.css('#optionForStatus'));
  //   selectOptions[1].nativeElement.click();
  //   fixture.detectChanges();
  //   expect(component.onChange).toHaveBeenCalled();
  // });

//   it('should be able to get the value text from a select (classic test)', () => {
//   fixture.detectChanges();
//   const compiledDom = fixture.debugElement.nativeElement;
//   const select = compiledDom.querySelector('#selectStatus');
//   select.click();
//   fixture.detectChanges();
//   const optionSelectList: NodeListOf<HTMLElement> = overlay.querySelectorAll('#optionForStatus');
//
//   optionSelectList[1].click();
//   fixture.detectChanges();
//
//   expect(select.textContent).toEqual('preparing');
// });

});

describe('TestingOrderCockpitComponentWithSortOrderData', () => {
  let component: OrderCockpitComponent;
  let fixture: ComponentFixture<OrderCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceSortStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(OrderCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should sort records of orders', () => {
    component.sort({
      active: 'Reservation Date',
      direction: 'asc',
    });
    expect(component.orders).toEqual(ascSortOrder.content);
    expect(component.totalOrders).toBe(8);
  });
});
