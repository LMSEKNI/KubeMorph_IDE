import { Route } from '@angular/router';
import { DemoComponent } from './demo.component';
import { Component } from '@angular/core';
import { ListComponent } from './workload-details/list/list.component';
import { DesccComponent } from './workload-details/descc/descc.component';
import { YmlFileComponent } from './addressource/yml-file/yml-file.component';
import { DynamicFormComponent } from './addressource/dynamic-form/component/dynamic-form.component';
import {WelcomeInterfaceComponent} from './welcome-interface/welcome-interface.component';
import {MonitoringConfigComponent} from './monitoring-validate/monitoring-config.component';
import {MonitoringChoiceComponent} from './monitoring-metrics/monitoring-choice.component';
import {MonitoringPrometheusComponent} from './monitoring-prometheus/monitoring-prometheus.component';
import { ExecTerminalComponent} from './exec-terminal/exec-terminal.component';
import { LogTerminalComponent} from './log-terminal/log-terminal.component';
import { TerminalTabManagerComponent} from './terminal-tab-manager/terminal-tab-manager.component';
import { HelmComponent} from './helm/helm.component';
import { HelmDetailsComponent} from './helm/helm-details/helm-details.component';
import {MonitoringComponent} from './monitoring/components/monitoring.component';
import {ListingComponent} from './workload-details/listing/Components/listing.component';

import { ReleasesComponent} from './helm/releases/releases.component';
import { ReleaseDetailsComponent} from './helm/releases/release-details/release-details.component';

export const routes: Route[] = [
  { path: '' , component: ListComponent},
  { path: 'test' , component: ListingComponent},
  { path: 'descressource', component: DesccComponent},
  { path: 'create-ymlfile' , component: YmlFileComponent},
  { path: 'create-dynamicform' , component: DynamicFormComponent},
  { path: 'exec-terminal' , component: ExecTerminalComponent},
  { path: 'log-terminal' , component: LogTerminalComponent},
  { path: 'tab-terminal' , component: TerminalTabManagerComponent},
  { path: 'welcome' , component: WelcomeInterfaceComponent},
  { path: 'config' , component: MonitoringConfigComponent},
  { path: 'config-monitoring' , component: MonitoringChoiceComponent},
  { path: 'prometheus' , component: MonitoringPrometheusComponent},
  { path: 'helm' , component: HelmComponent },
  { path: 'helm/helmdetails' , component: HelmDetailsComponent},
  { path: 'monitoring' , component: MonitoringComponent},
  { path: 'helm-release' , component: ReleasesComponent},
  { path: 'release-details' , component: ReleaseDetailsComponent},

];
