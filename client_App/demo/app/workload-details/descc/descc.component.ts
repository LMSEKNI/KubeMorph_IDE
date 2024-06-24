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
  errorMessage = '';
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
          console.log("descccccccccccc"+ description);
        },
        (error: any) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  parseResourceDescription(description: string): void {
    this.resourceDetails = [];

    // Split description into main sections
    const sections = description.split(/\n\s*\n/);

    sections.forEach(section => {
      const lines = section.split('\n');
      if (lines.length > 0) {
        const mainTitle = lines[0].trim();
        const subsections = lines.slice(1); // Exclude the main title line

        this.resourceDetails.push({ title: mainTitle, description: '' });

        subsections.forEach(subsection => {
          const [label, value] = subsection.split(':');
          if (label && value) {
            this.resourceDetails.push({ title: label.trim(), description: value.trim() });
          }
        });
      }
    });
  }


}
