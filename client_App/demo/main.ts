import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

import { DemoModule } from './app/demo.module';
import { environment } from './environments/environment';

if (environment.production) { enableProdMode(); }

platformBrowserDynamic().bootstrapModule(DemoModule);
