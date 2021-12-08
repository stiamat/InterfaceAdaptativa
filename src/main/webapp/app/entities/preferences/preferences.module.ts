import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { PreferencesComponent } from './preferences.component';
import { PreferencesDetailComponent } from './preferences-detail.component';
import { PreferencesUpdateComponent } from './preferences-update.component';
import { PreferencesDeleteDialogComponent } from './preferences-delete-dialog.component';
import { preferencesRoute } from './preferences.route';

@NgModule({
  imports: [
    InterfaceAdaptativaSharedModule,
    RouterModule.forChild(preferencesRoute),
  ],
  declarations: [
    PreferencesComponent,
    PreferencesDetailComponent,
    PreferencesUpdateComponent,
    PreferencesDeleteDialogComponent,
  ],
  entryComponents: [PreferencesDeleteDialogComponent],
})
export class InterfaceAdaptativaPreferencesModule {}
