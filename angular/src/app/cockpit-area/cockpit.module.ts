import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CoreModule } from '../core/core.module';

import { WaiterCockpitService } from './services/waiter-cockpit.service';
import { AdminCockpitService } from './services/admin-cockpit.service';
import { WindowService } from '../core/window/window.service';
import { PredictionService } from './services/prediction.service';
import { ClusteringService } from './services/clustering.service';

import { ReservationCockpitComponent } from './reservation-cockpit/reservation-cockpit.component';
import { OrderCockpitComponent } from './order-cockpit/order-cockpit.component';
import { OrderArchiveComponent } from './order-archive/order-archive.component';
import { OrderDialogComponent } from './order-cockpit/order-dialog/order-dialog.component';
import { ArchiveDialogComponent } from './order-archive/archive-dialog/archive-dialog.component';
import { ReservationDialogComponent } from './reservation-cockpit/reservation-dialog/reservation-dialog.component';
import { HttpClientModule } from '@angular/common/http';
import { PredictionCockpitComponent } from './prediction-cockpit/prediction-cockpit.component';
import { ClusteringCockpitComponent } from './clustering-cockpit/clustering-cockpit.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslocoRootModule } from '../transloco-root.module';
import { AdminCockpitComponent } from './admin-cockpit/admin-cockpit.component';
import { AdminDialogComponent } from './admin-cockpit/admin-dialog/admin-dialog.component';
import { TableCockpitComponent } from './table-cockpit/table-cockpit.component';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@NgModule({
  imports: [
    CommonModule,
    HttpClientModule,
    TranslocoRootModule,
    CoreModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [
    AdminCockpitService,
    WaiterCockpitService,
    WindowService,
    PredictionService,
    ClusteringService,
    { provide: MAT_DIALOG_DATA, useValue: [] },
  ],
  declarations: [
    ReservationCockpitComponent,
    OrderCockpitComponent,
    OrderArchiveComponent,
    ReservationDialogComponent,
    OrderDialogComponent,
    ArchiveDialogComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
    AdminCockpitComponent,
    AdminDialogComponent,
    TableCockpitComponent,
  ],
  exports: [
    ReservationCockpitComponent,
    TableCockpitComponent,
    OrderCockpitComponent,
    OrderArchiveComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
  ],
  entryComponents: [
    ReservationDialogComponent,
    OrderDialogComponent,
    OrderCockpitComponent,
    ArchiveDialogComponent,
    PredictionCockpitComponent,
    ClusteringCockpitComponent,
  ],
})
export class WaiterCockpitModule {}
