import { Component, OnInit } from '@angular/core';
import { CreateressourceService } from './service/createressource.service';
import {Examples} from '../dynamic-form/component/example-schemas.model';


@Component({
  selector: 'app-yml-file',
  templateUrl: './yml-file.component.html',
  styleUrls: ['./yml-file.component.scss']
})
export class YmlFileComponent {
  successMessage: string;

  constructor(private createressourceService: CreateressourceService) { }

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
