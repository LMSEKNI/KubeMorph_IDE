import { Component, OnInit, ViewChild } from '@angular/core';
import { MatMenuTrigger } from '@angular/material/menu';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { Examples } from './addressource/dynamic-form/component/example-schemas.model';
import { JsonPointer } from '@ajsf/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'demo',
  templateUrl: 'demo.component.html',
  styleUrls: ['demo.component.scss'],
  animations: [
    trigger('expandSection', [
      state('in', style({ height: '*' })),
      transition(':enter', [
        style({ height: 0 }), animate(100),
      ]),
      transition(':leave', [
        style({ height: '*' }),
        animate(100, style({ height: 0 })),
      ]),
    ]),
  ],
})
export class DemoComponent {

  constructor(private router: Router) { } 
  
  buttonText = '+';
  showChoices = false;
  selectedChoice: string | null = null;

 
  selectChoice(choice: string) {
    this.selectedChoice = choice;
    console.log('Selected choice:', choice);
    
    if (choice === 'Yml File') {
      this.router.navigate(['/create-ymlfile']); 
    }if (choice === 'Form') {
      this.router.navigate(['/create-dynamicform']);
    }


    }
}
