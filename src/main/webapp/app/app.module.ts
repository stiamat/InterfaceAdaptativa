import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { InterfaceAdaptativaSharedModule } from 'app/shared/shared.module';
import { InterfaceAdaptativaCoreModule } from 'app/core/core.module';
import { InterfaceAdaptativaAppRoutingModule } from './app-routing.module';
import { InterfaceAdaptativaHomeModule } from './home/home.module';
import { InterfaceAdaptativaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { MaterialModule } from './material-module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';

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
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class InterfaceAdaptativaAppModule {}
