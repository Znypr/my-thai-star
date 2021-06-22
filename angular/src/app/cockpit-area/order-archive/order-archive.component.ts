import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
import { Title } from '@angular/platform-browser';
import { TranslocoService } from '@ngneat/transloco';
import * as moment from 'moment';
import {Subscription} from 'rxjs';
import {ConfigService} from '../../core/config/config.service';
import {FilterCockpit, Pageable,} from '../../shared/backend-models/interfaces';
import {OrderListView} from '../../shared/view-models/interfaces';
import {WaiterCockpitService} from '../services/waiter-cockpit.service';
import {ArchiveDialogComponent} from './archive-dialog/archive-dialog.component';

@Component({
  selector: 'app-cockpit-order-archive',
  templateUrl: './order-archive.component.html',
  styleUrls: ['./order-archive.component.scss'],
  // uses order-cockpit scss, it should allways look the same!
})
export class OrderArchiveComponent implements OnInit, OnDestroy {
  private translocoSubscription = Subscription.EMPTY;
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: any[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', {static: true}) pagingBar: MatPaginator;

  orders: OrderListView[] = [];
  totalOrders: number;

  data: any;
  columnss: any[];
  columns: any[];

  displayedColumns: string[] = [
    'booking.bookingDate',
    'booking.email',
    'booking.bookingToken',
    'orderStatus',
  ];

  pageSizes: number[];

  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
    orderStatus: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    this.pageSizes = this.configService.getValues().pageSizes;
    this.data = dialogData;
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
      .selectTranslateObject('cockpit.order-archive.orderStatus', {}, lang)
      .subscribe((cockpitOrders) => {
        this.columnss = [
          {name: 'preparing', label: cockpitOrders.open},
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
          {name: 'booking.bookingDate', label: cockpitTable.reservationDateH},
          {name: 'booking.email', label: cockpitTable.emailH},
          {name: 'booking.bookingToken', label: cockpitTable.bookingTokenH},
          {name: 'orderStatus', label: cockpitTable.orderStatusH},
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
            if (entry.order.orderStatus == "cancelled" && entry.order.paid == false || entry.order.orderStatus == "delivered" && entry.order.paid == true) {
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
    this.dialog.open(ArchiveDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  onChange(orderStatus: string, element: any): void {
    element.order.orderStatus = orderStatus;
    this.waiterCockpitService
      .updateOrderStatus(element.order.id, orderStatus)
      .subscribe((data: any) => {
        this.applyFilters();
      });
  }

  disableCurrentStatusOption(orderStatus: string, element: any) {
    if (orderStatus == element.order.orderStatus) return true;
    else return false;
  }

  checkIfCancelled(element: any) {
    if (element.order.orderStatus == "cancelled") return true;
    else return false;
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }

}
