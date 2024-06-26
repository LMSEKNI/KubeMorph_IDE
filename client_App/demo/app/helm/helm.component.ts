import { Component, OnInit } from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import {Router} from '@angular/router';

@Component({
  selector: 'app-helm',
  templateUrl: './helm.component.html',
  styleUrls: ['./helm.component.scss']
})
export class HelmComponent implements OnInit {
  longText = `The Shiba Inu is the smallest of the six original and distinct spitz breeds of dog
  from Japan. A small, agile dog that copes very well with mountainous terrain, the Shiba Inu was
  originally bred for hunting.`;

  constructor(private  router :Router) { }

  ngOnInit(): void {
  }

  navigateToDetail(): void {
    this.router.navigate(['/helm/helmdetails']); // Navigate to HelmDetailComponent
  }


}
