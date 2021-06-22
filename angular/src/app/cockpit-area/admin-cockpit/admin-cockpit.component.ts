import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { AdminCockpitService } from '../services/admin-cockpit.service';
import { ConfigService } from '../../core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { UserListView, UserView } from '../../shared/view-models/interfaces';
import { Subscription } from 'rxjs';
import * as moment from 'moment';
import {
  FilterAdminCockpit,
  Pageable,
} from '../../shared/backend-models/interfaces';
import { AdminDialogComponent } from './admin-dialog/admin-dialog.component';
import { Title } from '@angular/platform-browser';


@Component({
  selector: 'app-admin-cockpit',
  templateUrl: './admin-cockpit.component.html',
  styleUrls: ['./admin-cockpit.component.scss']
})
export class AdminCockpitComponent implements OnInit, OnDestroy {
  // private translocoSubscription = Subscription.EMPTY;
  hide = true;

  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: any[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;
  //
  users: UserListView[] = [];
  totalUsers: number;
  //
  columns: any[];
  //
  displayedColumns: string[] = [
    'user.id',
    'user.username',
    'user.email',
    'user.idRole',
    // 'userrole.idRole',
  ];

  pageSizes: number[];


  filters: FilterAdminCockpit = {
    id: undefined,
    username: undefined,
    email: undefined,
    idRole: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private adminCockpitService: AdminCockpitService,
    private configService: ConfigService,
    title: Title
  ) {
    title.setTitle('Admin Menu');
    this.pageSizes = this.configService.getValues().pageSizes;
  }

  // setTableHeaders(lang: string): void {
  //   this.translocoSubscription = this.translocoService
  //     .selectTranslateObject('user.table', {}, lang)
  //     .subscribe((cockpitTable) => {
  //       this.columns = [
  //         { name: 'booking.bookingDate', label: cockpitTable.reservationDateH },
  //         { name: 'booking.email', label: cockpitTable.emailH },
  //         { name: 'booking.bookingToken', label: cockpitTable.bookingTokenH },
  //       ];
  //     });
  // }

  applyFilters(): void {
    this.adminCockpitService
      .getUsers(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        if (!data) {
          this.users = [];
        } else {
          this.users = data.content;
        }
        this.totalUsers = data.totalElements;
      });
  }

  selected(selection: UserListView): void {
    this.dialog.open(AdminDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  // tslint:disable-next-line:typedef
  getUserInput(event: any) {
    const info = [
      event.target.Username.value,
      event.target.Email.value,
      event.target.Role.value,
      event.target.Password.value
    ];
    const responseOfCreation = this.adminCockpitService.addUser(info[0], info[1], info[2], info[3]).subscribe(res => {
      this.applyFilters();
    });
    return responseOfCreation;
  }

  //
  // ngOnDestroy(): void {
  //   this.translocoSubscription.unsubscribe();
  // }

  // THIS NEEDS TO BE HERE TO WORK
  ngOnInit() {
    this.applyFilters();
  }

  ngOnDestroy() {
  }
}
