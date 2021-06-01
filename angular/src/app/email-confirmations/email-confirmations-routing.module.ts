import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { EmailConfirmationsComponent } from './container/email-confirmations/email-confirmations.component';

const emailConfirmationsRoutes: Routes = [
  {
    path: 'invitedguest/accept/:token',
    component: EmailConfirmationsComponent,
  },
  {
    path: 'invitedguest/decline/:token',
    component: EmailConfirmationsComponent,
  },
  { path: 'booking/cancel/:token', component: EmailConfirmationsComponent },
  {
    path: 'booking/cancelOrder/:token',
    component: EmailConfirmationsComponent,
  }
];

@NgModule({
  imports: [RouterModule.forChild(emailConfirmationsRoutes)],
  exports: [RouterModule],
})
export class EmailConfirmationsRoutingModule {}
