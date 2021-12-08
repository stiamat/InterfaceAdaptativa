import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { InterfaceAdaptativaCoreModule } from 'app/core/core.module';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { InterfaceAdaptativaAppRoutingModule } from './app-routing.module';
import { InterfaceAdaptativaEntityModule } from './entities/entity.module';
import { InterfaceAdaptativaHomeModule } from './home/home.module';
import { ErrorComponent } from './layouts/error/error.component';
import { FooterComponent } from './layouts/footer/footer.component';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { SearchComponent } from './layouts/search/search.component';
import { MaterialModule } from './material-module';
import './vendor';

@NgModule({
  imports: [
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    BrowserModule,
    InterfaceAdaptativaSharedModule,
    InterfaceAdaptativaCoreModule,
    InterfaceAdaptativaHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    InterfaceAdaptativaEntityModule,
    InterfaceAdaptativaAppRoutingModule,
  ],
  declarations: [
    MainComponent,
    NavbarComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,
    SearchComponent,
  ],
  bootstrap: [MainComponent],
})
export class InterfaceAdaptativaAppModule {}
