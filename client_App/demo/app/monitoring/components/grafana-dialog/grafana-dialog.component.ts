import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {MonitoringService} from '../../services/monitoring.service';


@Component({
  selector: 'app-grafana-dialog',
  templateUrl: './grafana-dialog.component.html',
  styleUrls: ['./grafana-dialog.component.scss']
})
export class GrafanaDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<GrafanaDialogComponent>,
    private monitoringService: MonitoringService
  ) { }

  ngOnInit(): void {
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  navigateToExternalUrl(): void {
    this.monitoringService.getGrafanaServiceUrl().subscribe(
      (url: string) => {
        window.open(url, '_blank');
      },
      (error: any) => {
        console.error('Failed to fetch Grafana URL', error);
      }
    );
  }
}
