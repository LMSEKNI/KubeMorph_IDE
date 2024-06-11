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
import { YmlFileComponent } from './addressource/yml-file/yml-file.component';
import { NgTerminalModule } from 'ng-terminal';
import { MatSidenavModule } from '@angular/material/sidenav';
import { ListComponent } from './workload-details/list/list.component';
import { DesccComponent } from './workload-details/descc/descc.component';
import { DynamicFormComponent } from './addressource/dynamic-form/component/dynamic-form.component';
import { YmlFileUpdateComponent } from './updateressource/yml-file-update/yml-file-update.component';
import { DynamicFormUpdateComponent } from './updateressource/dynamic-form-update/dynamic-form-update.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatChipsModule } from '@angular/material/chips';
import { ExecTerminalComponent } from './exec-terminal/exec-terminal.component';
import { LogTerminalComponent } from './log-terminal/log-terminal.component';
import { TerminalTabManagerComponent } from './terminal-tab-manager/terminal-tab-manager.component';
import {MatTabsModule} from '@angular/material/tabs';


@NgModule({
  declarations: [
                  DemoComponent,
                  DemoRootComponent,
                  YmlFileComponent,
                  DesccComponent,
                  ListComponent,
                  DynamicFormComponent,
                  YmlFileUpdateComponent,
                  DynamicFormUpdateComponent,
                  ExecTerminalComponent,
                  LogTerminalComponent,
                  TerminalTabManagerComponent,

                  ],
    imports: [
        BrowserModule, BrowserAnimationsModule, FlexLayoutModule, FormsModule, MatChipsModule,
        HttpClientModule, MatButtonModule, MatCardModule, MatCheckboxModule, MatDialogModule,
        MatIconModule, MatMenuModule, MatSelectModule, MatToolbarModule,
        RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'}),
        Bootstrap4FrameworkModule,
        Bootstrap3FrameworkModule,
        MaterialDesignFrameworkModule,
        JsonSchemaFormModule,
        ReactiveFormsModule, MatSidenavModule, NgTerminalModule, MatTabsModule
    ],
  bootstrap: [DemoRootComponent]
})

export class DemoModule { }
