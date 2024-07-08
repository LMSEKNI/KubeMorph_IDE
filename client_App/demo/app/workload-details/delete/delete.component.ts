import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DeleteService} from '../service/delete.service';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit {
  resourceName: any;
  resourceType: string;

  constructor(
    private deleteService: DeleteService,
    private dialogRef: MatDialogRef<DeleteComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {  resourceType: string, resource: any }
  ) {
    this.resourceName = data.resource.metadata?.name;
    this.resourceType = data.resourceType;
  }

  ngOnInit(): void {}

  deleteressource(): void {
    this.deleteService.deleteResource(this.resourceType, this.resourceName)
      .subscribe({
        next: (response) => {
          console.log('Resource deleted successfully:', response);
          // Handle success response
          this.dialogRef.close(true); // Close dialog and indicate success
        },
        error: (error) => {
          console.error('Error deleting resource:', error);
          // Handle error response
          this.dialogRef.close(false); // Close dialog and indicate failure
        }
      });
  }
}
