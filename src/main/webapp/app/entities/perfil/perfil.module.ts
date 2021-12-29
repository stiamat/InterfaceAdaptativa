import { NgModule } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'app/material-module';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { SearchModule } from '../search/search.module';
import { PerfilComponent } from './perfil.component';
import { perfilRoute } from './perfil.route';

const ENTITY_STATE = [...perfilRoute];

@NgModule({
  imports: [
    SearchModule,
    MaterialModule,
    MatTabsModule,
    MatTooltipModule,
    InterfaceAdaptativaSharedModule,
    RouterModule.forChild(ENTITY_STATE),
  ],
  declarations: [PerfilComponent],
  entryComponents: [PerfilComponent],
})
export class PerfilModule {}
