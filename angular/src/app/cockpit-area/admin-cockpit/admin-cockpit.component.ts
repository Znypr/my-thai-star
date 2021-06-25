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
import * as config from '../../config'
import { Title } from '@angular/platform-browser';


@Component({
  selector: 'app-admin-cockpit',
  templateUrl: './admin-cockpit.component.html',
  styleUrls: ['./admin-cockpit.component.scss']
})
export class AdminCockpitComponent implements OnInit, OnDestroy {
  // private translocoSubscription = Subscription.EMPTY;
  hide = true;
  config = config.config;

  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
    // total: 1,
  };
  private sorting: any[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;
  
  users: UserListView[] = [];
  totalUsers: number;
  resetTokenEntity:any;
  
  columns: any[];
  roles: any[];
  
  displayedColumns: string[] = [
    'user.username',
    'user.email',
    'user.idRole',
    'user.id',
  ];

  pageSizes: number[];
  
  
  filters: FilterAdminCockpit = {
    username: undefined,
    email: undefined,
    idRole: undefined,
    id: undefined,
  };
  
  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private adminCockpitService: AdminCockpitService,
    private configService: ConfigService,
    title: Title
    ) {
      title.setTitle('Admin Cockpit');
      this.pageSizes = this.configService.getValues().pageSizes;
    }
    
    ngOnInit() {
      this.applyFilters();
      this.translocoService.langChanges$.subscribe((event: any) => {
        this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    }

    setTableHeaders(lang: string): void {
        this.translocoService
          .selectTranslateObject('cockpit.users', {}, lang)
          .subscribe((cockpitTable) => {
            this.columns = [
            { name: 'username', label: cockpitTable.usernameH },
            { name: 'email', label: cockpitTable.emailH },
            { name: 'role', label: cockpitTable.roleH },
            { name: 'id', label: cockpitTable.idH },
            { name: 'password', label: cockpitTable.passwordH }
          ];
        });

        this.translocoService
          .selectTranslateObject('cockpit.users.roles', {}, lang)
          .subscribe((roles) => {
            this.roles = [
              { name: 'customer', label: roles.customer },
            { name: 'waiter', label: roles.waiter },
            { name: 'manager', label: roles.manager },
            { name: 'admin', label: roles.admin }
          ];
        });
    }

  onButtonClick(token: String){
    this.adminCockpitService.getUserIdByToken(token).subscribe(
      (data: any) => {
        if (!data) {
          this.resetTokenEntity = [];
          alert('Hallo');
        } else {
          this.resetTokenEntity = data;
        }
    });
  }

  funk(){
    console.log(this.resetTokenEntity);
    return true;
  }



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
      width: '60%',
      data: selection,
    });
  }

  getUserInput(event: any) {
    const info = [
      event.target.Username.value,
      event.target.Email.value,
      event.target.Role.value,
      event.target.Password.value
    ];

    if(event.target.Username.value != null && event.target.Email.value != null && event.target.Role.value != null && event.target.Password.value != null){
      const responseOfCreation = this.adminCockpitService.addUser(info[0], info[1], info[2], info[3]).subscribe(res => {
        this.applyFilters();
      },
        err =>
        {
          this.adminCockpitService.snackBar("E-Mail oder Nutzername schon vergeben", "Ok");
        });

    return responseOfCreation;
  }
    return null;
  }

  clearFilters(filters: any): void {
    filters.reset();
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  disable(filter: any) : boolean {
    if
    (
      filter.username == "" 
      || filter.email == ""
      || filter.role == ""
      || filter.password == ""
    ) return true;
      
    else return false;
    
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  ngOnDestroy() {
  }
}
