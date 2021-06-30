
import { inject, TestBed } from '@angular/core/testing';
import { OrderView } from '../../shared/view-models/interfaces';
import { PriceCalculatorService } from '../../sidenav/services/price-calculator.service';
import { WaiterCockpitService } from './waiter-cockpit.service';
import { ConfigService } from '../../core/config/config.service';
import { config } from '../../core/config/config';
import { provideMockStore, MockStore } from '@ngrx/store/testing';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { OverlayModule } from '@angular/cdk/overlay';
import { TranslocoService } from '@ngneat/transloco';
import { of } from 'rxjs/internal/observable/of';

import * as fromAuth from '../../user-area/store';
import { StoreModule, MemoizedSelector } from '@ngrx/store';

import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import {
  FilterCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import {
  BookingResponse,
  DishResponse,
  DishView,
  OrderDishResponse,
  OrderListView,
  OrderResponse,
  OrderViewResult,
  PlateView, SaveOrderResponse, TableResponse,
} from '../../shared/view-models/interfaces';

const pageable: Pageable = {
  pageSize: 8,
  pageNumber: 0,
  // total: 1,
};
const sorting: any[] = [];

const pageSize = 8;
const filters: FilterCockpit = {
  bookingDate: undefined,
  email: undefined,
  bookingToken: undefined,
  orderStatus: undefined,
  paid: undefined,
};

const configServiceStub = {
  getRestPathRoot: jasmine
    .createSpy('getRestPathRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/')),
  getRestServiceRoot: jasmine
    .createSpy('getRestServiceRoot')
    .and.returnValue(of('http://localhost:8081/mythaistar/services/rest/')),
};

const translocoServiceStub = {
  selectTranslateObject: of({

    alerts: {
      orderStatus: {
        statusSuccess: "Status change successful",
        statusFail: "Status change failed"
      },
      paid: {
        paidSuccess: "Payment successful registered",
        unpaidSuccess: "Payment successful removed",
        paidFail: "Payment failed"
      },
    },


    reservationDateH: 'Reservation Date',
    emailH: 'Email',
    bookingTokenH: 'Reference Number',
    orderStatusH: 'Status',
    ownerH: 'Owner',
    tableH: 'Table',
    creationDateH: 'Creation date',
  } as any),
};

fdescribe('WaiterCockpitService', () => {
  let initialState;
  let translocoService: TranslocoService;
  let httpTestingController: HttpTestingController;
  let waiterCockpitService: WaiterCockpitService;

  let mockStore: MockStore;
  let mockAuthTokenSelector: MemoizedSelector<fromAuth.AppState, string>;
  let mockAuthUsernameSelector: MemoizedSelector<fromAuth.AppState, string>;

  beforeEach(() => {
    initialState = { config };
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({ initialState }),
        // provideMockStore(),
        WaiterCockpitService,
        { provide: TranslocoService, useValue: translocoServiceStub },
        { provide: ConfigService, useValue: configServiceStub },
        PriceCalculatorService,
        // ConfigService,
        SnackBarService,
        MatSnackBar,
      ],
      imports: [HttpClientTestingModule,HttpClientModule, OverlayModule,],
    }).compileComponents();
    translocoService = TestBed.inject(TranslocoService);
    httpTestingController = TestBed.inject(HttpTestingController);
    waiterCockpitService = TestBed.inject(WaiterCockpitService);
  });

  it('should create', inject(
    [WaiterCockpitService],
    (service: WaiterCockpitService) => {
      expect(service).toBeTruthy();
    },
  ));

  it('should send getOrders-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.getOrders(pageable, sorting, filters).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'ordermanagement/v1/order/search');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send getReservations-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.getReservations(pageable, sorting, filters).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'bookingmanagement/v1/booking/search');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send getTables-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.getTables(pageable, sorting, filters).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'bookingmanagement/v1/table/search');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send addTable-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.addTable("name","alexaId").subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'bookingmanagement/v1/table/');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send deleteTable-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.deleteTable("0").subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'bookingmanagement/v1/table/0');
      expect(req.request.method).toEqual('DELETE');
      req.flush('Success');
  }));

  it('should send saveOrder-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.saveOrder({}).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'ordermanagement/v1/order');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send deleteOrder-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.deleteOrder(0).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'ordermanagement/v1/order0');
      expect(req.request.method).toEqual('GET');
      req.flush('Success');
  }));

  it('should send changeOrder-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.changeOrder({}).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'ordermanagement/v1/order/change');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));

  it('should send updateBooking-request to server', inject(
    [WaiterCockpitService], (service: WaiterCockpitService) => {
      service.updateBooking({}).subscribe();
      const req = httpTestingController.expectOne(config.restServiceRoot + 'bookingmanagement/v1/booking/update');
      expect(req.request.method).toEqual('POST');
      req.flush('Success');
  }));



  describe('Form composer', () => {
    it('should compose correctly order info', inject(
      [WaiterCockpitService],
      (service: WaiterCockpitService) => {
        const orderData: OrderView[] = [
          {
            dish: {
              id: 0,
              name: 'dish1',
              price: 14.75,
            },
            extras: [
              { id: 0, price: 1, name: 'Extra Curry' },
              { id: 1, price: 2, name: 'Extra pork' },
            ],
            orderLine: {
              amount: 2,
              comment: 'comment',
            },
          },
          {
            dish: {
              id: 0,
              name: 'dish2',
              price: 12.15,
            },
            extras: [{ id: 0, price: 1, name: 'Extra Curry' }],
            orderLine: {
              amount: 1,
              comment: '',
            },
          },
        ];

        const orderResult: any = [
          {
            dish: { id: 0, name: 'dish1', price: (14.75 + 1 + 2) * 2 },
            extras: 'Extra Curry, Extra pork',
            orderLine: { amount: 2, comment: 'comment' },
          },
          {
            dish: { id: 0, name: 'dish2', price: 12.15 + 1 },
            extras: 'Extra Curry',
            orderLine: { amount: 1, comment: '' },
          },
        ]; // 2 dishes + 1 extra curry + 2 extra pork // 1 extra curry

        expect(service.orderComposer(orderData)).toEqual(orderResult);
      },
    ));
  });
});
