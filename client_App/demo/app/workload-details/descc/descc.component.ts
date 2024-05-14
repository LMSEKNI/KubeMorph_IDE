import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { DesccService } from '../service/descc.service';

@Component({
  selector: 'app-descc',
  templateUrl: './descc.component.html',
  styleUrls: ['./descc.component.scss']
})
export class DesccComponent implements OnInit {
  @Input() resourceName: string | null = null;
  @Input() resourceType: string | null = null;

  resourceDetails: { title: string, description: string }[] = [];
  errorMessage: string = '';
  isFrameOpen: boolean = false;

  constructor(private descServiceService: DesccService) { }

  ngOnInit(): void {
    console.log('ngOnInit called');
    console.log(this.resourceName);
    console.log(this.resourceType);
    if (this.resourceName && this.resourceType) {
      this.getResourceDetails();
    }
  }

  closeFrame(): void {
    this.isFrameOpen = false;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.resourceName && changes.resourceName.currentValue  ) {
      this.getResourceDetails();
    }
  }

  getResourceDetails(): void {
    this.descServiceService.getResourceDescriptions(this.resourceType, this.resourceName)
      .subscribe(
        (description: string) => {
          this.parseResourceDescription(description);
        },
        (error: any) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  parseResourceDescription(description: string): void {
    const lines = description.split('\n');

    this.resourceDetails = [];
    let currentTitle = '';
    let currentDescription = '';

    lines.forEach(line => {
      const [label, value] = line.split(':');

      if (label && value) {
        if (currentTitle && currentDescription) {
          this.resourceDetails.push({ title: currentTitle, description: currentDescription });
        }
        currentTitle = label.trim();
        currentDescription = value.trim();
      } else {
        currentDescription += '\n' + line.trim();
      }
    });

    if (currentTitle && currentDescription) {
      this.resourceDetails.push({ title: currentTitle, description: currentDescription });
    }
    //console.log('Parsed Resource Details:', this.resourceDetails);

  }
  // parseResourceDescription(description: string): void {
  //   const lines = description.split('\n');
  //   this.resourceDetails = [];
  //   lines.forEach(line => {
  //     const [label, value] = line.split(':');
  //     if (label && value) {
  //       this.resourceDetails.push({ title: label.trim(), description: value.trim() });
  //     }
  //   });
  // }

}
