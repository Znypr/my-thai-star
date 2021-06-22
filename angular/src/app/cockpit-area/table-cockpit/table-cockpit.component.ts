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

  bookings: ReservationView[] = [];
  tables: ReservationView[] = [];
  totalReservations: number;

  columns: any[];
  displayedColumns: string[] = ['id', 'name', 'alexaDevice'];

  pageSizes: number[];

  selected = new FormControl("selected");


  filters: FilterCockpit = {
    bookingDate: undefined,
    email: undefined,
    bookingToken: undefined,
  };

  constructor(
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    private dialog: MatDialog,
    private configService: ConfigService,
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
      .getReservations(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.bookings = [];
          // this.tables = [];
        } else {
          this.bookings = data.content;
          
          // for(let booking of data.content) {
          //   let add: boolean = true;
          //   for(let table of this.tables) {
          //     if(booking.booking.tableId == table.booking.tableId) add = false;
          //   }
          //   if(add) this.tables.push(booking);
          // }
          // console.log(this.tables);
        } 
        
        this.totalReservations = data.totalElements;
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

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
