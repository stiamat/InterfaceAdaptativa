import { NgModule } from '@angular/core';
import { InterfaceAdaptativaFeedModule } from './feed/feed.module';
import { PerfilModule } from './perfil/perfil.module';
import { InterfaceAdaptativaPostModule } from './post/post.module';
import { InterfaceAdaptativaPreferencesModule } from './preferences/preferences.module';
import { SearchModule } from './search/search.module';

@NgModule({
  imports: [
    InterfaceAdaptativaPostModule,
    InterfaceAdaptativaFeedModule,
    PerfilModule,
    SearchModule,
    InterfaceAdaptativaPreferencesModule,
  ],
})
export class InterfaceAdaptativaEntityModule {}
