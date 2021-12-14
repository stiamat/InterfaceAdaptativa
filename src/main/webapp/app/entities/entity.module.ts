import { NgModule } from '@angular/core';
import { InterfaceAdaptativaFeedModule } from './feed/feed.module';
import { InterfaceAdaptativaPostModule } from './post/post.module';
import { SearchModule } from './search/search.module';

@NgModule({
  imports: [
    InterfaceAdaptativaPostModule,
    InterfaceAdaptativaFeedModule,
    SearchModule,
  ],
})
export class InterfaceAdaptativaEntityModule {}
