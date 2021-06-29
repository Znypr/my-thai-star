import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import * as moment from 'moment';
import { ConfigService } from '../../../core/config/config.service';
import { UserView, UserListView } from '../../../shared/view-models/interfaces';
import { AdminCockpitService } from '../../services/admin-cockpit.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.scss']
})
export class AdminDialogComponent implements OnInit {
  private fromRow = 0;
  private currentPage = 1;
  private translocoSubscription = Subscription.EMPTY;

  columns: any[];
  roles: any[];
  pageSize = 4;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  data: any;
  datat: UserListView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'user.username',
    'user.email',
    'user.idRole',
    'user.id'
  ];

  pageSizes: number[];
  filteredData: UserListView[] = this.datat;


  constructor(
    private snackBarService: SnackBarService,
    private translocoService: TranslocoService,
    private adminCockpitService: AdminCockpitService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  ngOnInit(): void {
    this.translocoService.langChanges$.subscribe((event: any) => {
        this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
    this.datat.push(this.data);
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
          ];
        });

        this.translocoService
          .selectTranslateObject('cockpit.users.roles', {}, lang)
          .subscribe((roles) => {
            this.roles = [
            { name: 'waiter', label: roles.waiter },
            { name: 'customer', label: roles.customer },
            { name: 'manager', label: roles.manager },
            { name: 'admin', label: roles.admin }
          ];
        });
    }


  // You have to subscribe to execute the observable,
  // which initiates the DELETE request
  deleteUser(userId:number){
    this.adminCockpitService.deleteUser(userId).subscribe();
    this.snackBarService.openSnack(this.translocoService.translate('alerts.deleteUser.success'), 3000, 'green');
    this.adminCockpitService.reloadPage('/admin');
  }

  sendPasswordResetMail(userId: number){
    this.adminCockpitService.sendPasswordResetMail(userId).subscribe();
    this.snackBarService.openSnack(this.translocoService.translate('alerts.resetPassword.notification'), 3000, 'green');
  }

  isDisabled(role: any) : boolean {
    // true if user is manager
    return role == 2;
  }

  ngOnDestroy() {
  }
}
