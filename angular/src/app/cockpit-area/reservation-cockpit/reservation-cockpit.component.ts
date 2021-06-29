import {WaiterCockpitService} from '../services/waiter-cockpit.service';
import {ReservationView} from '../../shared/view-models/interfaces';
import {Component, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {PageEvent, MatPaginator} from '@angular/material/paginator';
import {Sort} from '@angular/material/sort';
import {ReservationDialogComponent} from './reservation-dialog/reservation-dialog.component';
import {
  FilterCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import * as moment from 'moment';
import {ConfigService} from '../../core/config/config.service';
import {TranslocoService} from '@ngneat/transloco';
import {Subscription} from 'rxjs';
import {Title} from '@angular/platform-browser';
import {SortDirection} from "../../menu/components/menu-filters/filter-sort/filter-sort.component";

@Component({
  selector: 'app-cockpit-reservation-cockpit',
  templateUrl: './reservation-cockpit.component.html',
  styleUrls: ['./reservation-cockpit.component.scss'],
})
export class ReservationCockpitComponent implements OnInit, OnDestroy {
  private sorting: any[] = [];
  private translocoSubscription = Subscription.EMPTY;
  pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };

  @ViewChild('pagingBar', {static: true}) pagingBar: MatPaginator;

  reservations: ReservationView[] = [];
  totalReservations: number;

  columns: any[];
  displayedColumns: string[] = ['needHelp', 'owner', 'bookingDate', 'assistants', 'table'];

  pageSizes: number[];

  filters: FilterCockpit = {
    tableId: undefined,
    name: undefined,
  };

  tables: any[] = [
    {id: 0},
    {id: 1},
    {id: 2},
    {id: 3}
  ];


  constructor(
    title: Title,
    private waiterCockpitService: WaiterCockpitService,
    private translocoService: TranslocoService,
    private dialog: MatDialog,
    private configService: ConfigService,
  ) {
    title.setTitle('Reservations');
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  ngOnInit(): void {

    this.applyFilters();

    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });

    setInterval(() => {
      this.applyFilters(); // api call
    }, 10000);
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('cockpit.table', {}, lang)
      .subscribe((cockpitTable) => {
        this.columns = [
          {name: 'booking.needHelp', label: cockpitTable.needHelpH},
          {name: 'booking.owner', label: cockpitTable.ownerH},
          {name: 'booking.bookingDate', label: cockpitTable.reservationDateH},
          {name: 'booking.assistants', label: cockpitTable.assistantsH},
          {name: 'booking.table', label: cockpitTable.tableH},
          {name: 'booking.tableSelect', label: cockpitTable.tableSelect}
        ];
      });
  }

  getGuestAmount(element) : number {
    let amount = 1;
    for(let guest of element.invitedGuests) {
      if(guest.accepted) amount++;
    }
    return amount;
  }

  filter(): void {
    this.pageable.pageNumber = 0;
    this.applyFilters();
  }

  applyFilters(): void {
    const customPageable = new Pageable();
    customPageable.pageSize = 24;
    customPageable.pageNumber = 0;
    // customPageable.sort = {
      // property: 'id',
      // direction: SortDirection.DESC
    // };
    this.waiterCockpitService.getTables(customPageable, this.sorting, this.filters).subscribe((data: any) => {
      this.tables = data.content;
      console.log(this.tables);
    });

    this.waiterCockpitService
      .getReservations(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.reservations = [];
        } else {
          this.reservations = data.content;
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

  selected(selection: ReservationView): void {
    this.dialog.open(ReservationDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  noHelpNeeded(element: any): boolean {
    return !element.booking.needHelp;
  }

  onChange(tableId: any, reservation: any): void {
    console.log(tableId); // selected tableId of element
    reservation.booking.tableId = tableId;
    console.log(reservation);
    this.waiterCockpitService.updateBooking(reservation).subscribe();
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }
}
