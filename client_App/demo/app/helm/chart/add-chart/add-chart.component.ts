import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { HelmServicesService } from '../services/helm-services.service';
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-add-chart',
  templateUrl: './add-chart.component.html',
  styleUrls: ['./add-chart.component.scss']
})
export class AddChartComponent {

  releaseName: string;
  chartFolderPath: string;

  constructor(
    public dialogRef: MatDialogRef<AddChartComponent>,
    private helmService: HelmServicesService,
    private snackBar: MatSnackBar
  ) {}

  onSubmit(): void {
    if (this.releaseName && this.chartFolderPath) {
      console.log('Submitting with Release Name:', this.releaseName);
      console.log('Submitting with Chart Folder Path:', this.chartFolderPath);

      // Call your service method to install the chart
      this.helmService.installChart(this.chartFolderPath, this.releaseName).subscribe(
        response => {
          this.openSnackBar('Chart installed successfully', 'green-snackbar');
          this.dialogRef.close();
        },
        error => {
          console.error('Failed to add chart:', error);
        }
      );
    } else {
      console.error('Release name and chart folder path are required.');
      this.openSnackBar('Failed to install chart', 'red-snackbar');

    }
  }
  private openSnackBar(message: string, panelClass: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: [panelClass]
    });
  }
  onClose(): void {
    this.dialogRef.close();
  }
}
