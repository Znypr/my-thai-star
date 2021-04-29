import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ConfigService } from '../../../core/config/config.service';
import { UserView, UserListView } from '../../../shared/view-models/interfaces';
import { AdminCockpitService } from '../../services/admin-cockpit.service';


@Component({
  selector: 'app-admin-dialog',
  templateUrl: './admin-dialog.component.html',
  styleUrls: ['./admin-dialog.component.scss']
})
export class AdminDialogComponent implements OnInit {
  private fromRow = 0;
  private currentPage = 1;

  pageSize = 4;

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
    private adminCockpitService: AdminCockpitService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
    this.pageSizes = this.configService.getValues().pageSizesDialog;
  }

  page(pagingEvent: PageEvent): void {
    this.currentPage = pagingEvent.pageIndex + 1;
    this.pageSize = pagingEvent.pageSize;
    this.fromRow = pagingEvent.pageSize * pagingEvent.pageIndex;
    // this.filter();
  }

  // filter(): void {
  //   let newData: any[] = this.datat;
  //   newData = newData.slice(this.fromRow, this.currentPage * this.pageSize);
  //   setTimeout(() => (this.filteredData = newData));
  // }

// You have to subscribe to execute the observable,
// which initiates the DELETE request  
  deleteUser(userId:number){
    this.adminCockpitService.deleteUser(userId).subscribe();
  }



  ngOnInit(): void {
    this.datat.push(this.data);
    // this.filter()
  }

}
