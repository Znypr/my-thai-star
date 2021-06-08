import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { SnackBarService } from '../../../core/snack-bar/snack-bar.service';
import { InvitationResponse } from '../../../shared/view-models/interfaces';
import * as fromRoot from '../../../store';
import { EmailConfirmationsService } from '../../services/email-confirmations.service';
import { TranslocoService } from '@ngneat/transloco';

@Component({
  selector: 'app-public-email-confirmations',
  templateUrl: './email-confirmations.component.html',
  styleUrls: ['./email-confirmations.component.scss'],
})
export class EmailConfirmationsComponent implements OnInit {
  private action: string;
  private token: string;

  constructor(
    private snackBarService: SnackBarService,
    private emailService: EmailConfirmationsService,
    private translocoService: TranslocoService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {

    const errorString = this.translocoService.translate('alerts.genericError');
    const errorUrlString = this.translocoService.translate('alerts.urlError');


    this.route.paramMap.subscribe((params: ParamMap) => {
      this.token = params.get('token');
      this.action = params.get('action');

      switch (this.action) {
        case 'acceptInvite':
          this.emailService.sendAcceptInvitation(this.token).subscribe(
            (res: InvitationResponse) => {
              this.snackBarService.openSnack(
                this.translocoService.translate('alerts.emailConfirmations.invitationAccept'),
                10000,
                'green',
              );
            },
            (error: any) => {
              this.snackBarService.openSnack(errorString, 10000, 'red');
            },
          );
          break;
        case 'rejectInvite':
          this.emailService.sendRejectInvitation(this.token).subscribe(
            (res: InvitationResponse) => {
              this.snackBarService.openSnack(
                this.translocoService.translate('alerts.emailConfirmations.invitationReject'),
                10000,
                'red',
              );
            },
            (error: any) => {
              this.snackBarService.openSnack(errorString, 10000, 'red');
            },
          );
          break;
        case 'cancel':
          this.emailService.sendCancelBooking(this.token).subscribe(
            (res: InvitationResponse) => {
              this.snackBarService.openSnack(
                this.translocoService.translate('alerts.emailConfirmations.bookingCancel'),
                10000,
                'green',
              );
            },
            (error: any) => {
              this.snackBarService.openSnack(errorString, 10000, 'red');
            },
          );
          break;
        case 'cancelOrder':
          this.emailService.sendCancelOrder(this.token).subscribe(
            (res: boolean) => {
              this.snackBarService.openSnack(
                this.translocoService.translate('alerts.emailConfirmations.orderCancel'),
                10000,
                'green',
              );
            },
            (error: any) => {
              this.snackBarService.openSnack(errorString, 10000, 'red');
            },
          );
          break;
        case 'test':
          break;
        default:
          this.snackBarService.openSnack(errorUrlString, 10000, 'black');
          break;
      }
    });
    this.router.navigate(['/restaurant']);
  }
}
