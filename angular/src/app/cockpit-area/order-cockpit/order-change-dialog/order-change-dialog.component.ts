import { Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { BookingView, DishView, ExtraView, OrderListView, OrderView, PlateView } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { getSelectors } from '@ngrx/router-store';
import { Booking } from 'app/book-table/models/booking.model';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { Filter, OrderLineInfo, Pageable } from 'app/shared/backend-models/interfaces';
import { MenuService } from 'app/menu/services/menu.service';

import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import { select, Store } from '@ngrx/store';
import * as fromMenu from '../../../menu/store';
import * as fromApp from '../../../store';
import { keyframes } from '@angular/animations';


@Component({
  selector: 'app-cockpit-order-dialog',
  templateUrl: './order-change-dialog.component.html',
  styleUrls: ['./order-change-dialog.component.scss'],
})
export class OrderChangeDialogComponent implements OnInit, OnDestroy {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;
  columnss: any[];

  orderlineInput = new FormControl();
  test = new FormControl();

  testDishes = [
    { id: 0, name: 'Corn' },
    { id: 1, name: 'Water' },
    { id: 2, name: 'Pepper' },
  ];

  dishes$: Observable<DishView[]> = this.store.select(fromMenu.getDishes);

  data: any;
  datat: BookingView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
    'tableId',
  ];

  removeComment: boolean;

  datao: OrderView[] = [];
  columnso: any[];
  columnsb: any[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'orderlineDelete',
  ];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  newOrderLines: OrderView[];
  totalPrice: number;

  constructor(
    private store: Store<fromApp.State>,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
    private snackbarServive: SnackBarService,
    private menuService: MenuService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {
    console.log(this.data.orderLines);

    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
    this.removeComment = false;

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposerChange(this.data.orderLines);
    this.newOrderLines = this.datao;
    this.datat.push(this.data.booking);
    this.filter();
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columnst = [
          { name: 'bookingDate', label: cockpitTable.reservationDateH },
          { name: 'creationDate', label: cockpitTable.creationDateH },
          { name: 'name', label: cockpitTable.ownerH },
          { name: 'email', label: cockpitTable.emailH },
          { name: 'tableId', label: cockpitTable.tableH },
        ];
      });

    this.translocoService
      .selectTranslateObject('cockpit.orders.dialogTable', {}, lang)
      .subscribe((cockpitDialogTable) => {
        this.columnso = [
          { name: 'dish.name', label: cockpitDialogTable.dishH },
          { name: 'orderLine.comment', label: cockpitDialogTable.commentsH },
          { name: 'extras', label: cockpitDialogTable.extrasH },
          { name: 'orderLine.amount', label: cockpitDialogTable.quantityH },
        ];
      });
  }

  page(pagingEvent: PageEvent): void {
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    this.filter();
  }

  filter(): void {
    let newData: any[] = this.datao;
    newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
    setTimeout(() => (this.filteredData = newData));

  }

  getDishes(): void {
    // let dishId: number;
    // let dishLabel: string;
    // let dishes = this.waiterCockpitService
    //   .getDishes()
    //   .subscribe((data: any) => {
    //     this.menu = [];
    //     for (let entry of data.content) {
    //         this.menu.push(entry);
    //         console.log(entry);
    //     }
    //   });
    // console.log(this.menu);
  }

  checkInvalidDelete(element: any): boolean {
    console.log(element.length);
    if (element.length > 1) return false;
    else return true;
  }

  reset() {
    this.snackbarServive.openSnack(
      this.translocoService.translate('alerts.orderChange.reset'),
      2000,
      'green',
    );
    this.ngOnInit();
  }

  apply() {
    //TODO
    // #1 post new order object to database

    if (this.waiterCockpitService.changeOrder(this.data.order.id, this.data))
      this.snackbarServive.openSnack(
        this.translocoService.translate('alerts.orderChange.applySuccess'),
        2000,
        'green',
      );
    else
      this.snackbarServive.openSnack(
        this.translocoService.translate('alerts.orderChange.applyFail'),
        2000,
        'red',
      );

    console.log(this.data);
  }

  addOrderline(): void {
    // this.menuService.getDishes(null);

    //dish = getDishById(this.test.value)
    //name: dish.name, price: dish.price

    // let orderline: OrderView = {
    //   dish: { id: this.orderlineInput.value, name: 'test', price: 13 },
    //   orderLine: { amount: 1, comment: '' },
    //   extras: [],
    // };

    let orderline: OrderView = {
      dish: { id: 0, name: this.test.value, price: 13 },
      orderLine: { amount: 1, comment: '' },
      extras: [],
    };

    this.newOrderLines.push(orderline);
    this.datao = this.waiterCockpitService.orderComposerChange(this.newOrderLines);
    this.filter();

    console.log(this.data.orderLines);
  }

  deleteOrderline(element: any): void {
    //TODO
    // #1 delete front end orderline
    // #2 remove orderline from order object

    // #1
    if (this.filteredData.length > 1) {
      let orderlines: OrderView[] = [];

      for (let orderline of this.datao) {
        if (orderline != element) orderlines.push(orderline);
      }
      this.newOrderLines = orderlines;
      this.datao = this.waiterCockpitService.orderComposerChange(this.newOrderLines);
      this.filter();

      this.snackbarServive.openSnack(
        this.translocoService.translate(
          'alerts.orderChange.deleteOrderlineSuccess',
        ),
        2000,
        'green',
      );
    } else {
      this.snackbarServive.openSnack(
        this.translocoService.translate(
          'alerts.orderChange.deleteOrderlineFail',
        ),
        2000,
        'red',
      );
    }
    // #2

    console.log(this.data.orderLines);
  }

  handleExtra(element: any, checked: boolean, extra: String) : void {
    console.log(checked);
    if(checked) this.addExtra(element, extra);
    else this.removeExtra(element, extra);
  }
  addExtra(element: any, extra: String): void {
    
    console.log('add: ', element);
    
    let newExtra : ExtraView;

    if (extra == 'tofu') {
      newExtra = {
        name: 'Tofu',
        id: 0,
        price: 1
      }
    }
      
    if (extra == 'curry') {
        newExtra = {
          name: 'Extra curry',
          id: 1,
          price: 1,
        };
    }

    if (element.extras.length < 2 && newExtra != undefined) {
      element.extras.push(newExtra);
    } 

    console.log(newExtra);
  }

  removeExtra(element: any, extra: String): void {
    
    console.log('remove: ', element);

    let int: number = 0;
    let newExtras : ExtraView[] = [];

    for(let extra of element.extras) {
      if(extra.name == 'Tofu' && extra != 'tofu' || extra.name == 'Extra curry' && extra != 'curry') {
        newExtras.push(extra);
      } 
    }

    element.extras = newExtras;
    
    console.log(newExtras);
  }

  extraSelected(element: any, extra: String): boolean {
    if (extra == 'tofu') {
      for(let extra of element.extras) {
        if(extra.name == 'Tofu') return true;
      }
        
      return false;
    }

    if (extra == 'curry') {
      for (let extra of element.extras) {
        if (extra.name == 'Extra curry') return true;
      }

      return false;
    }
  }

  isLastOrderline(): boolean {
    if (this.filteredData.length > 1) return false;
    else return true;
  }

  removeField(element: any): void {
    element.orderLine.comment = '';
  }

  onChange(orderStatus: string): void {
    this.data.order.orderStatus = orderStatus;
    this.ngOnInit();
    this.waiterCockpitService
      .updateOrderStatus(this.data.order.id, orderStatus)
      .subscribe();
  }

  ngOnDestroy(): void {}
}
