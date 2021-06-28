import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed, inject, flush, fakeAsync, tick } from '@angular/core/testing';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import { menuDishes } from '../../../../in-memory-test-data/db-menu-dish';
import { filteredMenuDishes } from '../../../../in-memory-test-data/db-menu-dish.filter';
import * as uuid from 'uuid';
import * as fromApp from '../../../../app/store/reducers';
import { CoreModule } from '../../../core/core.module';
import { PriceCalculatorService } from '../../../sidenav/services/price-calculator.service';
import { SidenavService } from '../../../sidenav/services/sidenav.service';
import { TranslocoService } from '@ngneat/transloco';
import { getTranslocoModule } from '../../../transloco-testing.module';
import { MenuCardDetailsComponent } from '../../../menu/components/menu-card/menu-card-details/menu-card-details.component';
import { MenuCardComponent } from '../../../menu/components/menu-card/menu-card.component';
import { MenuService } from '../../../menu/services/menu.service';
import { OrderChangeDialogComponent } from './order-change-dialog.component';
import { dialogOrderDetails } from '../../../../in-memory-test-data/db-order-dialog-data';
import { By } from '@angular/platform-browser';
import { of } from 'rxjs/internal/observable/of';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { MatDialog,MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WaiterCockpitModule } from '../../cockpit.module';
import { ConfigService } from '../../../core/config/config.service';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Store } from '@ngrx/store';
import { State } from '../../../store';


const mockFilterValue = {
  searchBy: 'fried',
  sort: {
    property: 'price',
    direction: 'DESC',
  },
  maxPrice: null,
  minLikes: null,
  categories: {
    mainDishes: false,
    starters: false,
    desserts: false,
    noodle: false,
    rice: true,
    curry: false,
    vegan: false,
    vegetarian: false,
    favourites: false,
  },
};

const menuServiceStub = {
  getDishes: jasmine.createSpy('getDishes').and.returnValue(menuDishes),
  menuToOrder: jasmine.createSpy('menuToOrder').and.returnValue({
    id: uuid.v4(),
    details: {
      dish: menuDishes.content[0].dish,
      extras: menuDishes.content[0].extras,
      orderLine: {
        amount: 1,
        comment: '',
      },
    },
  }),
  composeFilters: jasmine
    .createSpy('composeFilters')
    .and.returnValue(filteredMenuDishes),
};

const sidenavServiceStub = {
  openSideNav: jasmine.createSpy('openSideNav'),
};

const STATE = {
  menu: {
    menu: {
      loading: true,
      loaded: true,
      dishes: menuDishes.content as any[],
      errorMessage: '',
    },
  },
  sidenav: {
    order: {
      ids: [],
      entities: {},
      selectedOrderId: null,
      errorMessage: '',
      orderSend: {
        token: '',
        orderSent: false,
        error: '',
      },
    },
  },
  auth: {
    error: null,
    text: null,
    user: null,
    token: {
      token: '',
    },
  },
};

const waiterCockpitServiceStub = {
  // getOrders: jasmine.createSpy('getOrders').and.returnValue(of(orderData)),
  saveOrder: jasmine.createSpy('saveOrder').and.returnValue(of(dialogOrderDetails)),
  getTotalPrice: jasmine.createSpy('getTotalPrice').and.callThrough(),
  filter: jasmine.createSpy('filter').and.returnValue(of(null)),
  orderComposerChange: jasmine.createSpy('orderComposerChange').and.returnValue(of(dialogOrderDetails)),
};

xdescribe('OrderChangeDialogComponent', () => {
  let component: OrderChangeDialogComponent;
  let fixture: ComponentFixture<OrderChangeDialogComponent>;
  let priceCalculatorService: PriceCalculatorService;
  let el: DebugElement;
  let mockStore: MockStore<fromApp.State>;
  let store: Store<State>;
  let configService: ConfigService;
  let sidenavService: SidenavService;
  let waiterCockpitService: WaiterCockpitService;




  beforeEach(async(() => {
    TestBed.configureTestingModule({
      schemas: [NO_ERRORS_SCHEMA],
      declarations: [
        OrderChangeDialogComponent,
        MenuCardComponent,
        MenuCardDetailsComponent,
      ],
      providers: [
        PriceCalculatorService,
        TranslocoService,
        ConfigService,
        SnackBarService,
        { provide: SidenavService, useValue: sidenavServiceStub },
        // { provide: MenuService, useValue: menuServiceStub },
        MenuService,
        { provide: MAT_DIALOG_DATA, useValue: dialogOrderDetails },
        provideMockStore({ initialState: STATE }),
        { provide: WaiterCockpitService, useValue: waiterCockpitServiceStub },
      ],
      imports: [
                BrowserAnimationsModule,
                ReactiveFormsModule,
                WaiterCockpitModule,
                MatDialogModule,
                getTranslocoModule(),
                CoreModule,
      ],
    }).compileComponents().then(() => {
    mockStore = TestBed.inject(MockStore);
    store = TestBed.inject(Store);
    priceCalculatorService = TestBed.inject(PriceCalculatorService);
    sidenavService = TestBed.inject(SidenavService);
    fixture = TestBed.createComponent(OrderChangeDialogComponent);
    component = fixture.componentInstance;
    configService = new ConfigService(store);
    el = fixture.debugElement;
    waiterCockpitService = TestBed.inject(WaiterCockpitService);
    fixture.detectChanges();
    });
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create3', () => {
    expect(1).toBe(2);
  });

    it('should create 2', fakeAsync(() => {
      fixture.detectChanges();
      tick();
      const name = el.query(By.css('.nameData'));
      const email = el.query(By.css('.emailData'));
      expect(email.nativeElement.textContent.trim()).toBe('user0@mail.com');
      expect(name.nativeElement.textContent.trim()).toBe('user0');
      expect(component).toBeTruthy();
      expect(component.datat[0].bookingToken).toEqual(dialogOrderDetails.booking.bookingToken);
      expect(component).toBeFalsy();
    }));
});



//
//   it('should create', fakeAsync(() => {
//     fixture.detectChanges();
//     tick();
//     const name = el.query(By.css('.nameData'));
//     const email = el.query(By.css('.emailData'));
//     expect(email.nativeElement.textContent.trim()).toBe('user0@mail.com');
//     expect(name.nativeElement.textContent.trim()).toBe('user0');
//     expect(component).toBeTruthy();
//     expect(component.datat[0].bookingToken).toEqual(dialogOrderDetails.booking.bookingToken);
//     flush();
//   }));
//
//   it('test description', async(async () => {
//     fixture.detectChanges();
//     await fixture.whenStable();
//     const name = el.query(By.css('.nameData'));
//     const email = el.query(By.css('.emailData'));
//     expect(email.nativeElement.textContent.trim()).toBe('user0@mail.com');
//     expect(name.nativeElement.textContent.trim()).toBe('user0');
//     expect(component).toBeTruthy();
//     expect(component.datat[0].bookingToken).toEqual(dialogOrderDetails.booking.bookingToken);
// }));
//
//
//   //
//   // it('should get menu', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   expect(component.menu).not.toBeUndefined();
//   // });
//   //
//   // it('should get price', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   const dish = el.query(By.css('#dishPrice'));
//   //   expect(dish.nativeElement.textContent.trim()).not.toBeUndefined();
//   // });
//   //
//   // it('should reset on button click', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   const resetButton = el.query(By.css('#resetButton'));
//   //   spyOn(component,'reset');
//   //   click(resetButton);
//   //   expect(component.reset).toHaveBeenCalled();
//   // });
//   //
//   // it('should apply changes', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   component.apply();
//   //   expect(service.saveOrder).toHaveBeenCalled();
//   // });
//   //
//   // it('should add orderline', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   spyOn(component,'updateOrderlines');
//   //   const entry = el.query(By.css('#entryOption'));
//   //   click(entry);
//   //   component.addOrderline();
//   //   expect(component.updateOrderlines).toHaveBeenCalled();
//   // });
//   //
//   //
//   // it('should delete orderline', async() => {
//   //   fixture.detectChanges();
//   //   fixture.destroy();
//   //   spyOn(component, 'deleteOrderline');
//   //   const deleteButton = el.query(By.css('#deleteButton'));
//   //   click(deleteButton);
//   //   expect(component.deleteOrderline).toHaveBeenCalled();
//   // });
//
//
// });

// //   //
// //   // it('should reset on button click', async() => {
// //   //   fixture.detectChanges();
// //   //   fixture.destroy();
// //   //   const resetButton = el.query(By.css('#resetButton'));
// //   //   spyOn(component,'reset');
// //   //   click(resetButton);
// //   //   expect(component.reset).toHaveBeenCalled();
// //   // });
// //   //
// //   // it('should apply changes', async() => {
// //   //   fixture.detectChanges();
// //   //   fixture.destroy();
// //   //   component.apply();
// //   //   expect(service.saveOrder).toHaveBeenCalled();
// //   // });
// //   //
// //   // it('should add orderline', async() => {
// //   //   fixture.detectChanges();
// //   //   fixture.destroy();
// //   //   spyOn(component,'updateOrderlines');
// //   //   const entry = el.query(By.css('#entryOption'));
// //   //   click(entry);
// //   //   component.addOrderline();
// //   //   expect(component.updateOrderlines).toHaveBeenCalled();
// //   // });
// //   //
// //   //
// //   // it('should delete orderline', async() => {
// //   //   fixture.detectChanges();
// //   //   fixture.destroy();
// //   //   spyOn(component, 'deleteOrderline');
// //   //   const deleteButton = el.query(By.css('#deleteButton'));
// //   //   click(deleteButton);
// //   //   expect(component.deleteOrderline).toHaveBeenCalled();
// //   // });
// //
// //
// // });
