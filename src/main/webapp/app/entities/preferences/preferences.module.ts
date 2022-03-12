import { NgModule } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'app/material-module';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { PreferencesDeleteDialogComponent } from './preferences-delete-dialog.component';
import { PreferencesDetailComponent } from './preferences-detail.component';
import { PreferencesUpdateComponent } from './preferences-update.component';
import { PreferencesComponent } from './preferences.component';
import { preferencesRoute } from './preferences.route';

@NgModule({
  imports: [
    MaterialModule,
    MatTooltipModule,
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
