import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSelectModule } from '@angular/material/select';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { BookingView, OrderView } from '../../../shared/view-models/interfaces';
import { WaiterCockpitService } from '../../services/waiter-cockpit.service';
import { TranslocoService } from '@ngneat/transloco';
import { getSelectors } from '@ngrx/router-store';

@Component({
  selector: 'app-cockpit-archive-dialog',
  templateUrl: './archive-dialog.component.html',
  styleUrls: ['./archive-dialog.component.scss'],
})
export class ArchiveDialogComponent implements OnInit, OnDestroy {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;

  columnss: any[];

  data: any;
  datat: BookingView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = ['tableId', 'bookingDate', 'paid', 'orderStatus'];

  datao: OrderView[] = [];

  pageSizes: number[];
  filteredData: OrderView[] = this.datao;
  totalPrice: number;

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
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
          { name: 'tableId', label: cockpitTable.tableH },
          { name: 'bookingDate', label: cockpitTable.reservationDateH },
          { name: 'paid', label: cockpitTable.paidH },
          { name: 'orderStatus', label: cockpitTable.orderStatusH },
        ];
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

  ngOnDestroy(): void {}
}
