import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { MaterialModule } from 'app/material-module';

@NgModule({
  imports: [MaterialModule, InterfaceAdaptativaSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class InterfaceAdaptativaHomeModule {}
