import { NgModule } from '@angular/core';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MaterialModule } from 'app/material-module';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { SearchComponent } from './search.component';

@NgModule({
  imports: [MaterialModule, MatTooltipModule, InterfaceAdaptativaSharedModule],
  declarations: [SearchComponent],
  entryComponents: [SearchComponent],
  exports: [SearchComponent],
})
export class SearchModule {}
