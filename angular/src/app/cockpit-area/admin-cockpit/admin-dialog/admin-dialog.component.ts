import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {MatDialog} from '@angular/material/dialog';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {ConfigService} from '../../../core/config/config.service';
import {UserView, UserListView} from '../../../shared/view-models/interfaces';
import {AdminCockpitService} from '../../services/admin-cockpit.service';
import {DeleteDialogComponent} from './delete-dialog/delete-dialog.component';


@Component({
  selector: 'app-admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.scss']
})
export class AdminDialogComponent implements OnInit {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;

  @ViewChild('pagingBar', {static: true}) pagingBar: MatPaginator;

  data: any;
  datat: UserListView[] = [];
  columnst: any[];
  displayedColumnsT: string[] = [
    'user.id',
    'user.username',
    'user.email',
  ];

  // datao: UserView[] = [];
  // columnso: any[];
  // displayedColumnsO: string[] = [
  //   'user.id',
  //   'user.name',
  //   'user.email',
  // ];

  pageSizes: number[];
  filteredData: UserListView[] = this.datat;


  constructor(
    private dialog: MatDialog,
    private adminCockpitService: AdminCockpitService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }


// You have to subscribe to execute the observable,
// which initiates the DELETE request
  deleteUser(userId: number) {
    this.adminCockpitService.deleteUser(userId).subscribe();
    this.adminCockpitService.reloadPage('/admin');
  }

  sendPasswordResetMail(userId: number) {
    this.adminCockpitService.sendPasswordResetMail(userId).subscribe(
      (res) => {
        this.adminCockpitService.snackBar("Die Email wird verarbeitet und in Kürze versendet", "verstanden");
      });
  }

  selected(selection: any): void {
    this.dialog.open(DeleteDialogComponent, {
      width: '80%',
      data: selection,
    });
  }

  ngOnInit(): void {
    this.datat.push(this.data);
    // this.filter()
  }

}
