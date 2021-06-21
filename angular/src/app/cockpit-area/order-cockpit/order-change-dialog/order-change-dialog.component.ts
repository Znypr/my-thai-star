import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import {BookingView, DishView, OrderView, PlateView, SaveOrderResponse} from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import * as fromMenu from '../../../menu/store';
import * as fromApp from '../../../store';

import {exhaustMap, map} from 'rxjs/operators';
import { SortDirection } from 'app/menu/components/menu-filters/filter-sort/filter-sort.component';
import { FilterFormData } from 'app/menu/components/menu-filters/menu-filters.component';
import {OrderListInfo, Pageable} from 'app/shared/backend-models/interfaces';
import { Store } from '@ngrx/store';
import { MenuService } from 'app/menu/services/menu.service';
import * as fromOrder from "../../../sidenav/store/selectors/order.selectors";


@Component({
  selector: 'app-cockpit-order-dialog',
  templateUrl: './order-change-dialog.component.html',
  styleUrls: ['./order-change-dialog.component.scss'],
})

export class OrderChangeDialogComponent implements OnInit, OnDestroy {
  // Pageable
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;
  columnss: any[];

  data: any;

  // Booking
  datat: BookingView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
    'tableId',
  ];

  // OrderView
  datao: OrderView[] = [];
  columnso: any[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'dish.price',
    'orderlineDelete',
  ];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  newOrderLines: OrderView[];
  totalPrice: number;

  dishSelect = new FormControl;
  dishes$: Observable<DishView[]>;
  dishes: any;
  newDishes: any;

  removeComment: boolean;


  constructor(
    // private menuService: MenuService,
    private store: Store<fromApp.State>,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
    private snackbarServive: SnackBarService,
  ) {
    this.data = dialogData;
    console.log(dialogData);
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {

    this.dishes$ = this.store.select(fromMenu.getDishes);
    this.store
      .select(fromMenu.getDishes)
      .subscribe((menu) => (this.dishes = menu));


    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });
    this.removeComment = false;

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposerChange(
      this.data.orderLines,
    );
    this.newOrderLines = this.datao;
    this.datat.push(this.data.booking);
    this.filter();

    console.log('init: ', this.datao);
  }

  getPrice(): number {
    this.totalPrice = this.waiterCockpitService.getTotalPrice(this.datao);
    return this.totalPrice;
  }

  getDishPrice(element: any): number {
    return (
      (element.dish.price + element.extras.length) * element.orderLine.amount
    );
  }

  setTableHeaders(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columnst = [
          {name: 'bookingDate', label: cockpitTable.reservationDateH},
          {name: 'creationDate', label: cockpitTable.creationDateH},
          {name: 'name', label: cockpitTable.ownerH},
          {name: 'email', label: cockpitTable.emailH},
          {name: 'tableId', label: cockpitTable.tableH},
        ];
      });

    this.translocoService
      .selectTranslateObject('cockpit.orders.dialogTable', {}, lang)
      .subscribe((cockpitDialogTable) => {
        this.columnso = [
          {name: 'dish.name', label: cockpitDialogTable.dishH},
          {name: 'orderLine.comment', label: cockpitDialogTable.commentsH},
          {name: 'extras', label: cockpitDialogTable.extrasH},
          {name: 'orderLine.amount', label: cockpitDialogTable.quantityH},
          {name: 'dish.price', label: cockpitDialogTable.priceH},
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

  checkInvalidDelete(element: any): boolean {
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

  // tslint:disable-next-line:typedef
  apply() {
    
    console.log("preapply data:",this.data);
     console.log("preapply datao:",this.datao);
    this.waiterCockpitService.saveOrder(this.data).subscribe();

    this.datao = this.waiterCockpitService.orderComposerChange(
      this.data.orderLines,
    );
    this.newOrderLines = this.datao;

    console.log(this.datao);

    this.filter();

  }

  getDishById(dishId: number): PlateView {

    let newDish: PlateView;

    for (let entry of this.dishes) {
      if (entry.dish.id == dishId) {
        newDish = {
          id: entry.dish.id,
          name: entry.dish.name,
          description: entry.dish.description,
          price: entry.dish.price,
        }

      }
    }
    return newDish;
  }

  addOrderline(): void {

    let dish = this.getDishById(this.dishSelect.value);

    let orderline: any = {
      orderLine: {amount: 1, comment: ''},
      order: null,
      dish: dish,
      extras: [],
    };

    this.newOrderLines.push(orderline);
    this.datao = this.waiterCockpitService.orderComposerChange(
      this.newOrderLines
    );

    console.log("add:",this.datao);
    this.filter();
  }

  deleteOrderline(element: any): void {

    if (this.filteredData.length > 1) {
      let orderlines: any[] = [];

      for (let orderline of this.datao) {
        if (orderline != element) orderlines.push(orderline);
      }
      
      this.datao = this.waiterCockpitService.orderComposerChange(
        orderlines
      );
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
    console.log("remove:",this.datao);
  }

  handleExtra(element: any, checked: boolean, extra: String): void {
    if (checked) this.addExtra(element, extra);
    else this.removeExtra(element, extra);
  }

  addExtra(element: any, extra: String): void {

    let newExtra: any;

    if (extra == 'tofu') {
      newExtra = {
        modificationCounter: 1,
        id: 0,
        name: 'Tofu',
        description:
          'Also known as bean curd, is a food made by coagula…sing the resulting curds into soft white blocks. ',
        price: 1,
      };
    }

    if (extra == 'curry') {
      newExtra = {
        modificationCounter: 1,
        id: 1,
        name: 'Extra curry',
        description:
          'The common feature is the use of complex combinati…s, usually including fresh or dried hot chillies.',
        price: 1,
      };
    }

    if (element.extras.length < 2 && newExtra != undefined) {
      element.extras.push(newExtra);
    }
  }

  removeExtra(element: any, extra: String): void {

    let int: number = 0;
    let newExtras: any[] = [];

    for (let entry of element.extras) {
      if (
        (entry.name == 'Tofu' && extra != 'tofu') ||
        (entry.name == 'Extra curry' && extra != 'curry')
      ) {
        newExtras.push(entry);
      }
    }
    element.extras = newExtras;
  }

  extraSelected(element: any, extra: String): boolean {
    if (extra == 'tofu') {
      for (let extra of element.extras) {
        if (extra.name == 'Tofu') return true;
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

  isDrink(element: any): boolean {
    return element.dish.name == "Tea"
      || element.dish.name == "Beer";
  }

  validateQuantity(element: any): boolean {
    return element.orderLine.amount <= 1;
  }

  changeQuantity(element: any, type: String): void {

    if (type == 'increment') {
      element.orderLine.amount++;
    }

    if (type == 'decrement') {
      element.orderLine.amount--;
    }
  }

  changeComment(element: any, comment: String): void {
    element.orderLine.comment = comment;
  }

  removeField(element: any): void {
    element.orderLine.comment = '';
  }

  ngOnDestroy(): void {
  }

}
