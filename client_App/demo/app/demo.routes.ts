import { Route } from '@angular/router';
import { ListComponent } from './workload-details/list/list.component';
import { DesccComponent } from './workload-details/descc/descc.component';
import { YmlFileComponent } from './addressource/yml-file/yml-file.component';
import { DynamicFormComponent } from './addressource/dynamic-form/component/dynamic-form.component';
import {WelcomeInterfaceComponent} from './welcome-interface/welcome-interface.component';
import {MonitoringConfigComponent} from './monitoring-validate/monitoring-config.component';
import {MonitoringChoiceComponent} from './monitoring-metrics/monitoring-choice.component';
import {MonitoringPrometheusComponent} from './monitoring-prometheus/monitoring-prometheus.component';

export const routes: Route[] = [
  { path: '' , component: ListComponent},
  { path: 'descressource', component: DesccComponent},
  { path: 'create-ymlfile' , component: YmlFileComponent},
  { path: 'create-dynamicform' , component: DynamicFormComponent},
  { path: 'welcome' , component: WelcomeInterfaceComponent},
  { path: 'config' , component: MonitoringConfigComponent},
  { path: 'config-monitoring' , component: MonitoringChoiceComponent},
  { path: 'prometheus' , component: MonitoringPrometheusComponent},

];
