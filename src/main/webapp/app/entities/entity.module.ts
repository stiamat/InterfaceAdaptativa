import { NgModule } from '@angular/core';
import { InterfaceAdaptativaFeedModule } from './feed/feed.module';
import { InterfaceAdaptativaPostModule } from './post/post.module';

@NgModule({
  imports: [
    InterfaceAdaptativaPostModule,
    InterfaceAdaptativaFeedModule
  ],
})
export class InterfaceAdaptativaEntityModule {}
