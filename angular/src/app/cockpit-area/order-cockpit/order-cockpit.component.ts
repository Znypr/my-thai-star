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
// @ts-ignore
import { OrderChangeDialogComponent } from './order-change-dialog/order-change-dialog.component';
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
  columnsb: any[];
  columnss: any[];

  displayedColumns: string[] = [
    'booking.tableId',
    'booking.bookingDate',
    'booking.owner',
    'paid',
    'orderStatus',
    'orderEdit',
  ];

  pageSizes: number[];

  test: any;

  filters: FilterCockpit = {
    tableId: undefined,
    orderStatus: undefined,
    paid: undefined,
  };

  constructor(
    title: Title,
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private waiterCockpitService: WaiterCockpitService,
    private configService: ConfigService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
  ) {
    title.setTitle('Orders');
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
          { name: 'booking.tableId', label: cockpitTable.tableH },
          { name: 'booking.owner', label: cockpitTable.ownerH },
          { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
          { name: 'paid', label: cockpitTable.paidH },
          { name: 'orderStatus', label: cockpitTable.orderStatusH },
        ];
      });

       this.translocoSubscription = this.translocoService
      .selectTranslateObject('buttons', {}, lang)
      .subscribe((button) => {
        this.columnsb = [
          { name: 'orderEdit', label: button.action },
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

  checkOrderStatus() : string {
    if(this.filters.orderStatus != undefined)
      return this.filters.orderStatus;
    else return "all";
  }

  checkPaid() : string {
    if(this.filters.paid != undefined)
      if (this.filters.paid == true)
        return "true";
      else
        return "false";
    else return "all";
  }

  clearFilters(filters: any): void {
    this.filters.paid = undefined;
    this.filters.orderStatus = undefined;
    filters.reset();
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  filterPaid(value: any) : void {
    if(value == "all")  this.filters.paid = null;
    else if(value == "true") this.filters.paid = true;
    else this.filters.paid = false;
  }

  filterState(value: any) : void {
    if(value == "all")  this.filters.orderStatus = null;
    else this.filters.orderStatus = value;
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

  editOrder(selection: OrderListView): void {
    this.dialog.open(OrderChangeDialogComponent, {
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

  getTranslationPathState(orderStatus: string) : string {
    let path = "cockpit.orders.orderStatus.";

    if(orderStatus == "open") return path += "open";
    if(orderStatus == "preparing") return path += "preparing";
    if(orderStatus == "delivered") return path += "delivered";
    if(orderStatus == "cancelled") return path += "cancelled";
  }

  getTranslationPathPaid(paid: boolean) : string {
    let path = "cockpit.orders.payment.";

    if(paid) return path += "yes";
    else return path += "no";
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

  checkIfOpen(element: any) : boolean {
    if(element.order.orderStatus == "open") return false;
    else return true;
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
