import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { UpdateressourceService } from '../service/updateressource.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import {MessageDialogComponent} from './message-dialog/message-dialog.component';


@Component({
  selector: 'app-dynamic-form-update',
  templateUrl: './dynamic-form-update.component.html',
  styleUrls: ['./dynamic-form-update.component.scss']
})
export class DynamicFormUpdateComponent implements OnInit {
  @Input() namespace: string;
  @Input() resourceType: string;
  @Input() resourceName: string;
  resourceJson: any;
  editedResourceJson: string;
  errorMessage: string;
  successMessage: string;
  dynamicForm: FormGroup;
  formFields: Array<{ key: string, type: string, label: string }> = [];

  constructor(
    private updateressourceService: UpdateressourceService,
    private fb: FormBuilder,
    public dialog: MatDialog
  ) {
    this.dynamicForm = this.fb.group({});
  }

  ngOnInit(): void {
    this.getResourceDetails();
  }

  getResourceDetails(): void {
    this.updateressourceService.getResourceAsJson(this.namespace, this.resourceType, this.resourceName)
      .subscribe(
        (description) => {
          this.resourceJson = description;
          this.editedResourceJson = JSON.stringify(description, null, 2);
          this.createForm(description);
        },
        (error: HttpErrorResponse) => {
          this.showMessageDialog('Error', 'Error fetching resource details. Please try again later.');
        }
      );
  }

  createForm(resource: any): void {
    // Clear the previous form fields
    this.formFields = [];
    this.dynamicForm = this.fb.group({});

    // Add name and kind fields
    ['name', 'kind'].forEach(field => {
      if (resource[field]) {
        this.formFields.push({ key: field, type: typeof resource[field], label: field });
        const control = new FormControl(resource[field], Validators.required);
        control.valueChanges.subscribe(newValue => {
          this.updateResourceJson(field, newValue);
        });
        this.dynamicForm.addControl(field, control);
      }
    });

    // Handle the spec field
    if (resource.spec) {
      this.addSpecFields(resource.spec, 'spec');
    }
  }

  addSpecFields(spec: any, parentKey: string): void {
    Object.keys(spec).forEach(key => {
      const value = spec[key];
      const fieldType = typeof value;
      const formKey = `${parentKey}.${key}`;
      const label = `spec > ${key}`;

      this.formFields.push({ key: formKey, type: fieldType, label });

      if (fieldType === 'object' && value !== null && !Array.isArray(value)) {
        this.addSpecFields(value, formKey); // Recursively handle nested objects
      } else if (Array.isArray(value)) {
        value.forEach((item, index) => {
          this.addSpecFields(item, `${formKey}[${index}]`);
        });
      } else {
        const control = new FormControl(value, Validators.required);
        control.valueChanges.subscribe(newValue => {
          this.updateResourceJson(formKey, newValue);
        });
        this.dynamicForm.addControl(formKey, control);
      }
    });
  }

  updateResourceJson(key: string, value: any): void {
    const keys = key.split('.');
    let current = this.resourceJson;
    while (keys.length > 1) {
      const subKey = keys.shift();
      if (subKey.endsWith(']')) {
        const [arrayKey, index] = subKey.slice(0, -1).split('[');
        current = current[arrayKey][+index];
      } else {
        current = current[subKey];
      }
    }
    const finalKey = keys[0];
    if (finalKey.endsWith(']')) {
      const [arrayKey, index] = finalKey.slice(0, -1).split('[');
      current[arrayKey][+index] = value;
    } else {
      current[finalKey] = value;
    }
    this.editedResourceJson = JSON.stringify(this.resourceJson, null, 2);
  }

  onSubmit(): void {
    if (this.dynamicForm.invalid) {
      return;
    }

    try {
      const updatedJson = JSON.parse(this.editedResourceJson);
      this.updateressourceService.updateResource(this.namespace, this.resourceType, this.resourceName, updatedJson)
        .subscribe(
          (response) => {
            this.showMessageDialog('Success', 'Resource updated successfully.');
            this.getResourceDetails(); // Refresh resource details
          },
          (error: HttpErrorResponse) => {
            this.showMessageDialog('Error', 'Error updating resource. Please try again later.');
          }
        );
    } catch (e) {
      this.showMessageDialog('Error', 'Invalid JSON format. Please correct it and try again.');
    }
  }

  showMessageDialog(title: string, message: string): void {
    this.dialog.open(MessageDialogComponent, {
      data: { title, message }
    });
  }
}
