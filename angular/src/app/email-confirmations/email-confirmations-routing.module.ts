import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from 'app/home/container/home/home.component';

import { EmailConfirmationsComponent } from './container/email-confirmations/email-confirmations.component';


const emailConfirmationsRoutes: Routes = [

  { path: 'test', redirectTo: 'restaurant', pathMatch: 'full' },

  // {
  //   path: 'booking/acceptInvite/:token', redirectTo: '/booking/:action/:token'
  // },
  // {
  //   path: 'booking/rejectInvite/:token',
  //   component: EmailConfirmationsComponent,
  // },
  // { path: 'booking/cancel/:token', component: EmailConfirmationsComponent },
  // {
  //   path: 'booking/cancelOrder/:token',
  //   component: EmailConfirmationsComponent,
  // }
];

@NgModule({
  imports: [RouterModule.forChild(emailConfirmationsRoutes)],
  exports: [RouterModule],
})
export class EmailConfirmationsRoutingModule {}
