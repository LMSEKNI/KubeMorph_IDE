import { Component, Input, OnInit } from '@angular/core';
import { UpdateressourceService } from '../service/updateressource.service';
import { HttpErrorResponse } from '@angular/common/http';
import { JsonFormsAngularService } from '@jsonforms/angular';
import { updateCore } from '@jsonforms/core';


@Component({
  selector: 'app-dynamic-form-update',
  templateUrl: './dynamic-form-update.component.html',
  styleUrls: ['./dynamic-form-update.component.scss'],
  providers: [JsonFormsAngularService]
})
export class DynamicFormUpdateComponent implements OnInit {
  @Input() namespace: string ;
  @Input() resourceType: string;
  @Input() resourceName: string;
  resourceJson: any;
  editedResourceJson: string;
  errorMessage: string;
  successMessage: string;
  formData: any = {};

  constructor(
    private updateressourceService: UpdateressourceService,
    private jsonFormsService: JsonFormsAngularService
  ) { }

  ngOnInit(): void {
    this.getResourceDetails();
  }

  getResourceDetails(): void {
    this.updateressourceService.getResourceAsJson(this.namespace, this.resourceType, this.resourceName)
      .subscribe(
        (description) => {
          this.resourceJson = description;
          this.editedResourceJson = JSON.stringify(description, null, 2);
          this.initializeForm();
        },
        (error: HttpErrorResponse) => {
          console.error('Error fetching resource details:', error);
          this.errorMessage = 'Error fetching resource details. Please try again later.';
        }
      );
  }

  initializeForm(): void {
    const schema = this.generateSchema(this.resourceJson);
    const uischema = this.generateUISchema(this.resourceJson);

    this.jsonFormsService.updateCore({
      type: 'jsonforms/UPDATE_CORE',
      data: this.resourceJson,
      schema: schema,
      uischema: uischema
    });
  }

  generateSchema(data: any): any {
    return {
      type: "object",
      properties: {
        apiVersion: { type: "string" },
        kind: { type: "string" },
        metadata: {
          type: "object",
          properties: {
            annotations: { type: "object" },
            creationTimestamp: { type: "string" },
            labels: { type: "object" },
            name: { type: "string" },
            namespace: { type: "string" }
          }
        },
        spec: {
          type: "object",
          properties: {
            containers: { type: "array" }
          }
        }
      }
    };
  }

  generateUISchema(data: any): any {
    return {
      type: 'VerticalLayout',
      elements: [
        {
          type: "Control",
          scope: "#/properties/apiVersion"
        },
        {
          type: "Control",
          scope: "#/properties/kind"
        },
        {
          type: "Control",
          scope: "#/properties/metadata/properties/name"
        },
        {
          type: "Control",
          scope: "#/properties/metadata/properties/namespace"
        },
        {
          type: "Control",
          scope: "#/properties/spec/properties/containers"
        }
      ]
    };
  }

  onSubmit(): void {
    this.editedResourceJson = JSON.stringify(this.formData);
    this.saveUpdates();
  }

  saveUpdates(): void {
    try {
      const updatedJson = JSON.parse(this.editedResourceJson);
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
