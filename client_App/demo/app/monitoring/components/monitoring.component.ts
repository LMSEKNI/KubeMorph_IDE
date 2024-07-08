import { Component, OnInit } from '@angular/core';
import { MonitoringService } from '../services/monitoring.service';
import { EChartsOption } from 'echarts';
import { GrafanaDialogComponent } from './grafana-dialog/grafana-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import {Location} from '@angular/common';

@Component({
  selector: 'app-monitoring',
  templateUrl: './monitoring.component.html',
  styleUrls: ['./monitoring.component.scss']
})
export class MonitoringComponent implements OnInit {

  nodeMetrics: any;
  podMetrics: any;
  error: any;
  podChartOptions: any[] = [];
  nodeChartOptions: EChartsOption;
  grafanaUrl: string;

  constructor(
    private monitoringService: MonitoringService,
    private dialog: MatDialog,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.fetchNodeMetrics();
    this.fetchPodMetrics();
  }

  goBack(): void {
    this.location.back();
  }

  redirectToGrafana(): void {
    const dialogRef = this.dialog.open(GrafanaDialogComponent, {
      width: '500px',
      disableClose: true,
      data: { isLoading: true }
    });

    this.monitoringService.getPrometheusMetrics().subscribe(
      response => {
        console.log('Monitoring stack setup response:', response);
        dialogRef.componentInstance.isLoading = false;

        if (response.includes('Monitoring stack created')) {
          this.monitoringService.getGrafanaAdminPassword().subscribe(
            password => {
              console.log('Grafana Admin Password:', password);
              dialogRef.componentInstance.grafanaPassword = password;
            },
            err => {
              console.error('Error fetching Grafana Admin Password:', err);
              dialogRef.componentInstance.errorMessage = 'Error fetching Grafana Admin Password';
            }
          );
        } else {
          dialogRef.componentInstance.errorMessage = 'Failed to create monitoring stack';
        }
      },
      err => {
        console.error('Error setting up monitoring stack:', err);
        dialogRef.componentInstance.isLoading = false;
        dialogRef.componentInstance.errorMessage = 'Error setting up monitoring stack';
      }
    );

    dialogRef.afterClosed().subscribe(result => {
      console.log('Dialog closed:', result);
    });
  }

  fetchNodeMetrics(): void {
    this.monitoringService.getNodeMetrics().subscribe(
      data => {
        this.nodeMetrics = data;
        console.log('Node Metrics:', this.nodeMetrics);
        this.generateNodeChart();
      },
      err => this.error = err
    );
  }

  fetchPodMetrics(): void {
    this.monitoringService.getPodMetrics().subscribe(
      data => {
        this.podMetrics = data;
        console.log('Pod Metrics:', this.podMetrics);
        this.generatePodCharts();
      },
      err => this.error = err
    );
  }

  generateNodeChart(): void {
    const nodeNames = this.nodeMetrics.items.map(item => item.metadata.name);
    const cpuUsage = this.nodeMetrics.items.map(item => item.usage.cpu.number);
    const memoryUsage = this.nodeMetrics.items.map(item => item.usage.memory.number);

    this.nodeChartOptions = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['CPU Usage', 'Memory Usage']
      },
      xAxis: {
        type: 'category',
        data: nodeNames
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: 'CPU Usage',
          type: 'bar',
          data: cpuUsage
        },
        {
          name: 'Memory Usage',
          type: 'bar',
          data: memoryUsage
        }
      ]
    };
  }

  generatePodCharts(): void {
    console.log('Generating Pod Charts...');
    this.podChartOptions = this.podMetrics.items.map(pod => {
      const containerNames = pod.containers.map(container => container.name);
      const cpuUsage = pod.containers.map(container => container.usage.cpu.number);
      const memoryUsage = pod.containers.map(container => container.usage.memory.number);

      console.log(`Pod: ${pod.metadata.name}`);
      console.log('Container Names:', containerNames);
      console.log('CPU Usage:', cpuUsage);
      console.log('Memory Usage:', memoryUsage);

      return {
        title: {
          text: `Pod: ${pod.metadata.name}`
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['CPU Usage', 'Memory Usage']
        },
        xAxis: {
          type: 'category',
          data: containerNames
        },
        yAxis: [
          {
            type: 'value',
            name: 'CPU Usage',
            position: 'left',
            axisLine: {
              lineStyle: {
                color: '#5793f3'
              }
            }
          },
          {
            type: 'value',
            name: 'Memory Usage',
            position: 'right',
            axisLine: {
              lineStyle: {
                color: '#d14a61'
              }
            }
          }
        ],
        series: [
          {
            name: 'CPU Usage',
            type: 'bar',
            data: cpuUsage
          },
          {
            name: 'Memory Usage',
            type: 'bar',
            yAxisIndex: 1,
            data: memoryUsage
          }
        ]
      };
    });
    console.log('Pod Chart Options:', this.podChartOptions);
  }

  objectKeys(obj: any): string[] {
    return Object.keys(obj);
  }
}
