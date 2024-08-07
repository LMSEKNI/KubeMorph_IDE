import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule , ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSelectModule } from '@angular/material/select';
import { MatToolbarModule } from '@angular/material/toolbar';
import { DemoComponent } from './demo.component';
import { DemoRootComponent } from './demo-root.component';
import { routes } from './demo.routes';
import { JsonSchemaFormModule } from '@ajsf/core';
import { Bootstrap4FrameworkModule } from '@ajsf/bootstrap4';
import { Bootstrap3FrameworkModule } from '@ajsf/bootstrap3';
import { MaterialDesignFrameworkModule } from '@ajsf/material';
import { YmlFileComponent } from './addressource/yml-file/component/yml-file.component';
// import { MatDrawerModule } from '@angular/material/drawer';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ListComponent } from './workload-details/list/list.component';
import { DesccComponent } from './workload-details/descc/descc.component';
import { DynamicFormComponent } from './addressource/dynamic-form/component/dynamic-form.component';
import { YmlFileUpdateComponent } from './workload-details/updateressource/yml-file-update/yml-file-update.component';
import { DynamicFormUpdateComponent } from './workload-details/updateressource/dynamic-form-update/dynamic-form-update.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatChipsModule } from '@angular/material/chips';
import { WelcomeInterfaceComponent } from './welcome-interface/welcome-interface.component';
import { MonitoringConfigComponent } from './monitoring-validate/monitoring-config.component';
import { MonitoringChoiceComponent } from './monitoring-metrics/monitoring-choice.component';
import { MonitoringPrometheusComponent } from './monitoring-prometheus/monitoring-prometheus.component';
import { PopupComponent } from './welcome-interface/popUp/popup/popup.component';
import { ExecTerminalComponent } from './exec-terminal/exec-terminal.component';
import { LogTerminalComponent } from './log-terminal/log-terminal.component';
import { TerminalTabManagerComponent } from './terminal-tab-manager/terminal-tab-manager.component';
import {MatTabsModule} from '@angular/material/tabs';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import {NgTerminalModule} from 'ng-terminal';
import { HelmComponent } from './helm/chart/component/helm.component';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import { HelmDetailsComponent } from './helm/chart/helm-details/helm-details.component';
import { CharttemplateComponent } from './helm/chart/charttemplate/charttemplate.component';
import { DeleteComponent } from './workload-details/delete/delete.component';
import { MonitoringComponent } from './monitoring/components/monitoring.component';
import {EChartsDirective} from './monitoring/Directives/EChartsDirective';
import { GrafanaDialogComponent } from './monitoring/components/grafana-dialog/grafana-dialog.component';
import { NgxJsonViewerModule } from 'ngx-json-viewer';
import { MessageDialogComponent } from './workload-details/updateressource/dynamic-form-update/message-dialog/message-dialog.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {ListingComponent} from './workload-details/listing/Components/listing.component';


import { AngularSplitModule } from 'angular-split';
import { ReleasesComponent } from './helm/releases/component/releases.component';
import { ReleaseDetailsComponent } from './helm/releases/release-details/release-details.component';
import { DeleteReleaseComponent } from './helm/releases/delete-release/delete-release.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Charttemplate2Component } from './helm/chart/charttemplate2/charttemplate2.component';
import { MatPaginatorModule } from '@angular/material/paginator';
import { AddChartComponent } from './helm/chart/add-chart/add-chart.component';


@NgModule({
  declarations: [ MonitoringComponent,
                  GrafanaDialogComponent,
                  EChartsDirective,
                  DemoComponent,
                  DemoRootComponent,
                  YmlFileComponent,
                  DesccComponent,
                  ListComponent,
                  DynamicFormComponent,
                  YmlFileUpdateComponent,
                  DynamicFormUpdateComponent,
                  WelcomeInterfaceComponent,
                  MonitoringConfigComponent,
                  MonitoringChoiceComponent,
                  MonitoringPrometheusComponent,
                  PopupComponent,
                  ExecTerminalComponent,
                  LogTerminalComponent,
                  TerminalTabManagerComponent,
                  HelmComponent,
                  HelmDetailsComponent,
                  CharttemplateComponent,
                  DeleteComponent,
                  MonitoringComponent,
                  GrafanaDialogComponent,
                  ReleasesComponent,
                  ReleaseDetailsComponent,
                  DeleteReleaseComponent,
                  Charttemplate2Component,
                  MessageDialogComponent,
                  ListingComponent,
                  AddChartComponent


                  ],
  imports: [
    AngularSplitModule, BrowserModule, BrowserAnimationsModule, FlexLayoutModule, FormsModule, MatChipsModule,
    HttpClientModule, MatButtonModule, MatCardModule, MatCheckboxModule, MatDialogModule,
    MatIconModule, MatMenuModule, MatSelectModule, MatToolbarModule,
    RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'}),
    Bootstrap4FrameworkModule, MatSnackBarModule,
    Bootstrap3FrameworkModule,
    MatInputModule, MatFormFieldModule, MatProgressBarModule, MatCardModule,
    MaterialDesignFrameworkModule,
    JsonSchemaFormModule,
    ReactiveFormsModule, MatSidenavModule, NgTerminalModule, MatTabsModule,
    NgxJsonViewerModule,
    AngularSplitModule, MatProgressSpinnerModule, MatPaginatorModule

  ],
  bootstrap: [DemoRootComponent]
})

export class DemoModule { }
