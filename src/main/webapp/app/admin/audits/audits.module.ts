import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [InterfaceAdaptativaSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
