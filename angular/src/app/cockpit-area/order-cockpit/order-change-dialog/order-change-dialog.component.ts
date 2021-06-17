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
    
    console.log(this.datao);
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

    console.log(this.datao);

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

    let orderline: any = {
      orderLine: { amount: 1, comment: '' },
      order: null,
      dish: { id: 0, name: this.test.value, price: 13 },
      extras: [],
    };

    this.newOrderLines.push(orderline);
    this.datao = this.waiterCockpitService.orderComposerChange(this.newOrderLines);
    this.filter();

    console.log(this.datao);
  }

  deleteOrderline(element: any): void {
    //TODO
    // #1 delete front end orderline
    // #2 remove orderline from order object

    // #1
    if (this.filteredData.length > 1) {
      let orderlines: any[] = [];

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

    console.log(this.datao);
  }

  handleExtra(element: any, checked: boolean, extra: String) : void {
    console.log(extra, " checked: ", checked);
    if(checked) this.addExtra(element, extra);
    else this.removeExtra(element, extra);
  }

  addExtra(element: any, extra: String): void {
    
    console.log('pre add: ', this.datao);
    
    let newExtra : any;

    if (extra == 'tofu') {
      newExtra = {
        modificationCounter: 1,
         id: 0,
        name: "Tofu", 
        description: "Also known as bean curd, is a food made by coagula…sing the resulting curds into soft white blocks. ", 
        price: 1
      }
    }
      
    if (extra == 'curry') {
        newExtra = {
          modificationCounter: 1, 
          id: 1,
          name: "Extra curry", 
          description: "The common feature is the use of complex combinati…s, usually including fresh or dried hot chillies.", 
          price: 1
        }
    }

    if (element.extras.length < 2 && newExtra != undefined) {
      element.extras.push(newExtra);
    } 
    console.log('post add: ', this.datao);
  }

  removeExtra(element: any, extra: String): void {
    
    console.log('pre remove: ', this.datao);

    let int: number = 0;
    let newExtras : any[] = [];

    for(let entry of element.extras) {
      if(entry.name == 'Tofu' && extra != 'tofu' || entry.name == 'Extra curry' && extra != 'curry') {
        newExtras.push(entry);
      } 
    }

    element.extras = newExtras;
    
    console.log('post remove: ', this.datao);
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

  validateQuantity(element: any, type: String) : boolean {
    if(type == 'increment')
      return element.orderLine.amount >= 10;
    
    if(type == 'decrement')
      return element.orderLine.amount <= 1;
  }

  changeQuantity(element: any, type: String) : void {

    console.log("pre change: ", this.datao);

    if(type == "increment") {
      element.orderLine.amount++;
    }

    if(type == "decrement") {
      element.orderLine.amount--;
    }

    // update datao
    // this.filter();

    console.log("post change: ", this.datao);
  }

  changeComment(element: any, comment: String) : void {
    
    element.orderLine.comment = comment;
    console.log("post comment: ", this.datao[0].orderLine.comment);
  }

  removeField(element: any): void {
    element.orderLine.comment = '';
  }

  ngOnDestroy(): void {}
}
