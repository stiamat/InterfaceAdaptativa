import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { PostComponent } from './post.component';
import { PostDetailComponent } from './post-detail.component';
import { PostUpdateComponent } from './post-update.component';
import { PostDeleteDialogComponent } from './post-delete-dialog.component';
import { postRoute } from './post.route';

const ENTITY_STATE = [... postRoute]

@NgModule({
  imports: [InterfaceAdaptativaSharedModule, RouterModule.forChild(ENTITY_STATE)],
  declarations: [PostComponent, PostDetailComponent, PostUpdateComponent, PostDeleteDialogComponent],
  entryComponents: [PostComponent, PostDetailComponent, PostUpdateComponent, PostDeleteDialogComponent],
})
export class InterfaceAdaptativaPostModule {}
