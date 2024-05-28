import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {UpdateressourceService} from '../service/updateressource.service';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-yml-file-update',
  templateUrl: './yml-file-update.component.html',
  styleUrls: ['./yml-file-update.component.scss']
})
export class YmlFileUpdateComponent implements OnInit {
  @Input() resourceName: string;
  @Input() resourceType: string;
  jsonDescription: object;
  editedJsonDescription: string;
  errorMessage: string;

  constructor(private updateressourceService: UpdateressourceService) { }

  ngOnInit(): void {
    this.getResourceDetails();
  }
  getResourceDetails(): void {
    console.log('nameressource', this.resourceName);
    console.log('typeressource', this.resourceType);
    this.updateressourceService.getRessourceUpdate('default', this.resourceName)
      .subscribe(
        (description) => {
          this.jsonDescription = description;
          this.editedJsonDescription = JSON.stringify(description, null, 2); // Initialize editedJsonDescription
        },
        (error: any) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  saveUpdates(): void {
    try {
      const updatedJson = JSON.parse(this.editedJsonDescription);
      this.updateressourceService.updateResource(this.resourceType, this.resourceName, updatedJson)
        .subscribe(
          (response) => {
            console.log('Resource updated successfully:', response);
            this.getResourceDetails(); // Refresh resource details
          },
          (error: HttpErrorResponse) => {
            console.error('Error updating resource:', error);
            this.errorMessage = 'Error updating resource. Please try again later.';
          }
        );
    } catch (e) {
      console.error('Invalid JSON format:', e);
      this.errorMessage = 'Invalid JSON format. Please correct it and try again.';
    }
  }
}
