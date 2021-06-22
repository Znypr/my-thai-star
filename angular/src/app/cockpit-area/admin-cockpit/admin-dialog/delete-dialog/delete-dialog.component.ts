import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AdminCockpitService } from '../../../services/admin-cockpit.service';
import { ConfigService } from '../../../../core/config/config.service';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.scss']
})
export class DeleteDialogComponent implements OnInit {
    data: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private adminCockpitService: AdminCockpitService,
    private configService: ConfigService,
  ) {
    this.data = dialogData;
  }

  deleteUser(userId:number){
    console.log("id: ",userId)
    this.adminCockpitService.deleteUser(userId).subscribe();
    this.adminCockpitService.reloadPage('/admin');
  }

  ngOnInit(): void {

  }

}
