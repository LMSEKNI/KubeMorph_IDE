import { Component, OnInit } from '@angular/core';
import { CreateressourceService } from './service/createressource.service';
import {Examples} from '../dynamic-form/component/example-schemas.model';

import { Location } from '@angular/common';

interface Breadcrumb {
  label: string;
  url: string;
}
@Component({
  selector: 'app-yml-file',
  templateUrl: './yml-file.component.html',
  styleUrls: ['./yml-file.component.scss']
})
export class YmlFileComponent {
  constructor( private location: Location,
               private createressourceService: CreateressourceService) { }

  successMessage: string;
  goBack(): void {
    this.location.back();
  }
  triggerFileInput() {
    const fileInput = document.getElementById('fileInputAlt') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      // Handle the file selection
      this.successMessage = `File ${file.name} selected successfully.`;
    }
  }



  onSubmit() {
    const fileInput = document.getElementById('fileInputAlt') as HTMLInputElement;
    const file = fileInput.files[0];
    if (file) {
      this.sendYamlToBackend(file);
    }
  }

  private sendYamlToBackend(file: File) {
    const formData = new FormData();
    formData.append('yamlFile', file);

    this.createressourceService.addResource(formData).subscribe(
      response => {
        this.successMessage = 'Resource added successfully';
        console.log('Resource added successfully:', response);
      },
      error => {
        console.error('Error adding resource:', error);
      }
    );
  }


}
