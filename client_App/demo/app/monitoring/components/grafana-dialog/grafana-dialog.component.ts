import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MonitoringService } from '../../services/monitoring.service';

@Component({
  selector: 'app-grafana-dialog',
  templateUrl: './grafana-dialog.component.html',
  styleUrls: ['./grafana-dialog.component.scss']
})
export class GrafanaDialogComponent implements OnInit {
  isLoading: boolean;
  grafanaPassword: string | null = null;
  errorMessage: string | null = null;
  grafanaUrl: string | null = null;

  constructor(
    public dialogRef: MatDialogRef<GrafanaDialogComponent>,
    private monitoringService: MonitoringService,
    @Inject(MAT_DIALOG_DATA) public data: { isLoading: boolean }
  ) {
    this.isLoading = data.isLoading;
  }

  ngOnInit(): void {
    // Initial logic if needed
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  navigateToExternalUrl(): void {
    this.monitoringService.getGrafanaServiceUrl().subscribe(
      (url: string) => {
        this.grafanaUrl = url;
        window.open(url, '_blank');
      },
      (error: any) => {
        console.error('Failed to fetch Grafana URL', error);
      }
    );
  }
}
