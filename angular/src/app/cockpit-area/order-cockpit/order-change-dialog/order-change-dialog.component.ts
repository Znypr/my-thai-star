import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { BookingView, OrderView } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { getSelectors } from '@ngrx/router-store';
import { Booking } from 'app/book-table/models/booking.model';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';

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

  data: any;
  datat: BookingView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'bookingDate',
    'creationDate',
    'name',
    'email',
  ];

  datao: OrderView[] = [];
  columnso: any[];
  displayedColumnsO: string[] = [
    'dish.name',
    'orderLine.comment',
    'extras',
    'orderLine.amount',
    'orderlineDelete'
  ];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  totalPrice: number;

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
    private snackbarServive: SnackBarService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
    });

    this.totalPrice = this.waiterCockpitService.getTotalPrice(
      this.data.orderLines,
    );
    this.datao = this.waiterCockpitService.orderComposer(this.data.orderLines);
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
          { name: 'orderlineDelete', label: cockpitDialogTable.orderlineDeleteH },
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

  checkInvalidDelete(element: any) : boolean {
    console.log(element.length);
    if(element.length > 1) return false;
    else return true;
  }

  reset() {

    this.snackbarServive.openSnack(this.translocoService.translate('alerts.orderChange.reset'), 2000, "green");
    this.ngOnInit();
  }

  apply() {
     
    //TODO
    // #1 post new order object to database
    
    //if (success)
    this.snackbarServive.openSnack(this.translocoService.translate('alerts.orderChange.applySuccess'), 2000, "green");
    //else
    this.snackbarServive.openSnack(this.translocoService.translate('alerts.orderChange.applyFail'), 2000, "red");
    this.ngOnDestroy();
  }

  deleteOrderline(element: any) : void {
    //TODO
    // #1 delete front end orderline
    // #2 remove orderline from order object

    this.snackbarServive.openSnack(this.translocoService.translate('alerts.orderChange.deleteOrderline'), 2000, "green");
  }



  onChange(orderStatus: string): void {
    console.log('Status: ', orderStatus);
    this.data.order.orderStatus = orderStatus;
    this.ngOnInit();
    this.waiterCockpitService
      .updateOrderStatus(this.data.order.id, orderStatus)
      .subscribe();
  }

  ngOnDestroy(): void {}
}
