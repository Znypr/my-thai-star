import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { TranslocoService } from '@ngneat/transloco';
import { SnackBarService } from 'app/core/snack-bar/snack-bar.service';
import { PasswordDialogComponent } from 'app/user-area/components/password-dialog/password-dialog.component';
import { AdminCockpitService } from '../services/admin-cockpit.service';

@Component({
  selector: 'app-reset-password-cockpit',
  templateUrl: './reset-password-cockpit.component.html',
  styleUrls: ['./reset-password-cockpit.component.scss']
})
export class ResetPasswordCockpitComponent implements OnInit {
  hide = true;
  token: String;
  userId: number;
  isValid= false;
  resetTokenEntity: any;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  constructor(
    private snackBarService: SnackBarService,
    private translocoService: TranslocoService,
   private route: ActivatedRoute,
   private router: Router,
   private adminCockpitService: AdminCockpitService
  ) { }


  ngOnInit(): void {
    this.route.queryParamMap.subscribe(
      (params) => {
        this.token = params.get('token');
        this.userId = Number(params.get('id'));
      }
    );
  }

  changePassword(event: any){
    const info = [
      event.target.Password.value,
      event.target.confirmPassword.value,
    ];
    if(info[0]==info[1]){
      this.adminCockpitService.changePassword(this.userId,info[0],this.token).subscribe(
        () => this.snackBarService.openSnack(this.translocoService.translate('alerts.resetPassword.success'), 3000, 'green'));
      
      this.adminCockpitService.reloadPage('/restaurant');
    } else {
      this.snackBarService.openSnack(this.translocoService.translate('alerts.resetPassword.fail'), 3000, 'red');
    }
  }

  reset(form: any) : void {
    form.reset();
  }


}
