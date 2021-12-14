import { NgModule } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterModule } from '@angular/router';
import { MaterialModule } from 'app/material-module';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { SearchModule } from '../search/search.module';
import { FeedComponent } from './feed.component';
import { feedRoute } from './feed.route';

const ENTITY_STATE = [...feedRoute];

@NgModule({
  imports: [
    SearchModule,
    MaterialModule,
    MatTooltipModule,
    InterfaceAdaptativaSharedModule,
    RouterModule.forChild(ENTITY_STATE),
  ],
  declarations: [FeedComponent],
  entryComponents: [FeedComponent],
})
export class InterfaceAdaptativaFeedModule {}
