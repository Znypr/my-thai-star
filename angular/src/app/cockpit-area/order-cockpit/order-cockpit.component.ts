import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { Title } from '@angular/platform-browser';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { ConfigService } from '../../core/config/config.service';
import {
  FilterCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import { OrderListView } from '../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { OrderDialogComponent } from './order-dialog/order-dialog.component';

@Component({
  selector: 'app-cockpit-order-cockpit',
  templateUrl: './order-cockpit.component.html',
  styleUrls: ['./order-cockpit.component.scss'],
})
export class OrderCockpitComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: any[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  orders: OrderListView[] = [];
  totalOrders: number;

  data: any;
  columns: any[];
  columnss: any[];

  displayedColumns: string[] = [
    'booking.bookingDate',
    'booking.email',
    'booking.bookingToken',
    'paid',
    'orderStatus',
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
    orderStatus: undefined,
    paid: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
    title: Title
  ) {
    title.setTitle('Orders');
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.applyFilters();
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      this.setOrderStatus(event);
      moment.locale(this.translocoService.getActiveLang());
    });

    setInterval(() => {
      this.applyFilters(); // api call
    }, 10000);
  }

  setOrderStatus(lang: string): void {
    this.translocoService
      .selectTranslateObject('cockpit.orders.orderStatus', {}, lang)
      .subscribe((cockpitOrders) => {
        this.columnss = [
          { name: 'open', label: cockpitOrders.open },
          { name: 'preparing', label: cockpitOrders.preparing },
          { name: 'delivered', label: cockpitOrders.delivered },
          {
            name: 'cancelled',
            label: cockpitOrders.cancelled,
          },
        ];
      });
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'booking.email', label: cockpitTable.emailH },
          { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
          { name: 'paid', label: cockpitTable.paidH },
          { name: 'orderStatus', label: cockpitTable.orderStatusH },
        ];
      });
  }

  applyFilters(): void {
    this.waiterCockpitService
      .getOrders(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.orders = [];
        } else {
          this.orders = [];
          for (let entry of data.content) {
             if (!(entry.order.orderStatus == "cancelled" && entry.order.paid == false || entry.order.orderStatus == "delivered" && entry.order.paid == true)) {
              this.orders.push(entry);
            }
          }
        }
        this.totalOrders = this.orders.length;
      });
  }

  clearFilters(filters: any): void {
    filters.reset();
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  sort(sortEvent: Sort): void {
    this.sorting = [];
    if (sortEvent.direction) {
      this.sorting.push({
        property: sortEvent.active,
        direction: '' + sortEvent.direction,
      });
    }
    this.applyFilters();
  }

  selected(selection: OrderListView): void {
    this.dialog.open(OrderDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  updatePaid(paid: any, element: any): void {
    element.order.paid = paid.checked;
    this.waiterCockpitService
      .updatePaid(element.order.id, paid.checked)
      .subscribe((data: any) => {
      this.applyFilters();
    });
  }

  onChange(orderStatus: string, element: any ): void {
    element.order.orderStatus = orderStatus;
    this.waiterCockpitService
      .updateOrderStatus(element.order.id, orderStatus)
      .subscribe((data: any) => {
      this.applyFilters();
    });
  }

  disableOption(orderStatus: string, element: any) : boolean {
    return this.disableCurrentStatusOption(orderStatus, element) || this.checkValidStatusTransition(orderStatus, element);
  }

   disableCurrentStatusOption(orderStatus: string, element: any): boolean {
    if(orderStatus == element.order.orderStatus) return true;
    else return false;
  }

  checkValidStatusTransition(orderStatus: string, element: any): boolean {
    if(element.order.orderStatus == "delivered" && orderStatus == "open") return true;
    if(element.order.orderStatus == "preparing" && orderStatus == "open") return true;
    else return false;
  }


  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
