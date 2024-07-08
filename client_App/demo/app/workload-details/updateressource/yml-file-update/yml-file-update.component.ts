import { Component, Input, OnInit } from '@angular/core';
import { UpdateressourceService } from '../service/updateressource.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-yml-file-update',
  templateUrl: './yml-file-update.component.html',
  styleUrls: ['./yml-file-update.component.scss']
})
export class YmlFileUpdateComponent implements OnInit {
  @Input() namespace: string;
  @Input() resourceType: string;
  @Input() resourceName: string;
  @Input() resource: any;
  ResourceYaml: string;
  errorMessage: string;
  successMessage: string;

  constructor(private updateressourceService: UpdateressourceService) { }

  ngOnInit(): void {
    this.getResourceDetails();
    // tslint:disable-next-line:no-unused-expression
    console.log(this.resourceType);
    console.log(this.resource);
    console.log(this.resource.metadata?.namespace);
  }

  getResourceDetails(): void {
    this.updateressourceService.getResourceAsYaml(this.resource.metadata?.namespace,
                                                  this.resourceType,
                                                  this.resource.metadata?.name)
      .subscribe(
        (yaml: string) => {
          this.ResourceYaml = yaml;
        },
        (error: HttpErrorResponse) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  saveUpdates(): void {
    this.updateressourceService.updateResource(this.resource.metadata?.namespace,
                                               this.resourceType,
                                               this.resource.metadata?.name,
                                               this.ResourceYaml)
      .subscribe(
        (response: string) => {
          console.log('Resource updated successfully:', response);
          this.successMessage = response;
          this.getResourceDetails(); // Refresh resource details
        },
        (error: HttpErrorResponse) => {
          console.error('Error updating resource:', error);
          this.errorMessage = 'Error updating resource. Please try again later.';
        }
      );
  }
}
