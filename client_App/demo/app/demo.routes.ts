import { Route } from '@angular/router';
import { DemoComponent } from './demo.component';
import { Component } from '@angular/core';
import { ListComponent } from './workload-details/list/list.component';
import { DesccComponent } from './workload-details/descc/descc.component';
import { YmlFileComponent } from './addressource/yml-file/yml-file.component';
import { DynamicFormComponent } from './addressource/dynamic-form/component/dynamic-form.component';
import { ExecTerminalComponent} from './exec-terminal/exec-terminal.component';

export const routes: Route[] = [
  { path: '' , component: ListComponent},
  { path: 'descressource', component: DesccComponent},
  { path: 'create-ymlfile' , component: YmlFileComponent},
  { path: 'create-dynamicform' , component: DynamicFormComponent},
  { path: 'exec-terminal' , component: ExecTerminalComponent},

];
