import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { ReservationView, BookingView } from '../../shared/view-models/interfaces';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent, MatPaginator } from '@angular/material/paginator';
import { Sort } from '@angular/material/sort';
// import { ReservationDialogComponent } from './reservation-dialog/reservation-dialog.component';
import {
  FilterCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import * as moment from 'moment';
import { ConfigService } from '../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { Subscription } from 'rxjs';
import { Title } from '@angular/platform-browser';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-cockpit-table-cockpit',
  templateUrl: './table-cockpit.component.html',
  styleUrls: ['./table-cockpit.component.scss'],
})
export class TableCockpitComponent implements OnInit, OnDestroy {
  private sorting: any[] = [];
  private translocoSubscription = Subscription.EMPTY;
  pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  alexaId: any = "321321321";
  bookings: ReservationView[] = [];
  tables: ReservationView[] = [];
  totalReservations: number;

  columns: any[];
  displayedColumns: string[] = ['id', 'name', 'alexaDevice', 'delete'];

  pageSizes: number[];

  selected = new FormControl("selected");


  filters: any = {
    name: undefined,
    alexaId: undefined
  };

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    private dialog: MatDialog,
    private configService: ConfigService,
    private snackbar: SnackBarService,
    title: Title
  ) {
    title.setTitle('Table Overview');
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.applyFilters();
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.tables', {}, lang)
      .subscribe((cockpitTables) => {
        this.columns = [
          { name: 'id', label: cockpitTables.idH },
          { name: 'name', label: cockpitTables.nameH },
          { name: 'alexaDevice', label: cockpitTables.alexaDeviceH },
          { name: 'alexaSelection', label: cockpitTables.alexaSelect },
        ];
      });
  }

  filter(): void {
    this.pageable.pageNumber = 0;
    this.applyFilters();
  }

  applyFilters(): void {
    this.waiterCockpitService
      .getTables(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.bookings = [];
        } else {
          this.bookings = data.content;
        }

        this.totalReservations = data.totalElements;
      });
  }

  clearFilters(filters: any): void {
    filters.reset();
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
      // total: 1,
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

  onChange(alexaId: any) : void {

    console.log(alexaId);
  }

  addTable(filters: any) : void {

    // TODO
    // delete table from database

    // if(successful)
    this.snackbar.openSnack(
            this.translocoService.translate('alerts.table.addSuccess'),
            4000,
            'green',
          );
    // else
    this.snackbar.openSnack(
            this.translocoService.translate('alerts.table.addFail'),
            4000,
            'red',
          );
  }

  removeTable(table: any) : void {

    // TODO
    // delete table from database

    // if(successful)
    this.snackbar.openSnack(
            this.translocoService.translate('alerts.table.deleteSuccess'),
            4000,
            'green',
          );
    // else
    this.snackbar.openSnack(
            this.translocoService.translate('alerts.table.deleteFail'),
            4000,
            'red',
          );

  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
