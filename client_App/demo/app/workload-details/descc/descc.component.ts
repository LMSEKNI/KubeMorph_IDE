import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { DesccService } from '../service/descc.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-descc',
  templateUrl: './descc.component.html',
  styleUrls: ['./descc.component.scss']
})
export class DesccComponent implements OnInit {
  @Input() resourceName: string | null = null;
  @Input() resourceType: string | null = null;
  @Input() resource: any;

  resourceJson: any;
  errorMessage = '';
  isFrameOpen = false;
  constructor(private descServiceService: DesccService,
              private route: ActivatedRoute) { }

  ngOnInit(): void {
    console.log(this.resource);
    this.route.queryParams.subscribe(params => {
      if (params && params['resource']) {
        this.resource = JSON.parse(params['resource']);
        console.log('Resource details:', this.resource);
      }
    });

    if (this.resourceName && this.resourceType) {
      this.getResourceDetails();
    }
  }

  closeFrame(): void {
    this.isFrameOpen = false;
  }

  getResourceDetails(): void {
    this.descServiceService.getResourceDescriptions(this.resourceType, this.resourceName)
      .subscribe(
        (description: string) => {
          this.resourceJson = this.parseDescription(description);
        },
        (error: any) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  parseDescription(description: string): any {
    const lines = description.split('\n');
    const result = {};
    let currentLevel = result;
    const levels = [result];

    lines.forEach(line => {
      const indentLevel = line.search(/\S|$/);
      const trimmedLine = line.trim();
      const [key, ...rest] = trimmedLine.split(':');
      const value = rest.join(':').trim();

      while (indentLevel < levels.length - 1) {
        levels.pop();
        currentLevel = levels[levels.length - 1];
      }

      if (value) {
        currentLevel[key.trim()] = value;
      } else {
        currentLevel[key.trim()] = {};
        levels.push(currentLevel[key.trim()]);
        currentLevel = currentLevel[key.trim()];
      }
    });
    return result;
  }
}
