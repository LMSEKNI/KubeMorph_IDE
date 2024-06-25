import { Component, Input, OnInit } from '@angular/core';
import { UpdateressourceService } from '../service/updateressource.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-dynamic-form-update',
  templateUrl: './dynamic-form-update.component.html',
  styleUrls: ['./dynamic-form-update.component.scss']
})
export class DynamicFormUpdateComponent implements OnInit {
  @Input() namespace: String = 'default';
  @Input() resourceType: string;
  @Input() resourceName: string;
  resourceJson: object;
  editedResourceJson: string;
  errorMessage: string;
  successMessage: string;


  constructor(private updateressourceService: UpdateressourceService) { }

  ngOnInit(): void {
    this.getResourceDetails();
  }

  getResourceDetails(): void {
    // @ts-ignore
    this.updateressourceService.getResourceAsJson(this.namespace,
      this.resourceType,
      this.resourceName)
      .subscribe(
        (description) => {
          this.resourceJson = description;
          this.editedResourceJson = JSON.stringify(description, null, 2);
        },
        (error: HttpErrorResponse) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  saveUpdates(): void {
    try {
      const updatedJson = JSON.parse(this.editedResourceJson);
      // @ts-ignore
      // tslint:disable-next-line:max-line-length
      this.updateressourceService.updateResource(this.namespace, this.resourceType, this.resourceName, updatedJson)
        .subscribe(
          (response) => {
            console.log('Resource updated successfully:', response);
            this.successMessage = response;
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
