import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MonitoringService {

  private baseUrl = 'http://localhost:8082/api/metrics';

  constructor(private http: HttpClient) { }

  getNodeMetrics(): Observable<any> {
    return this.http.get(`${this.baseUrl}/k8s/nodes`);
  }

  getPodMetrics(): Observable<any> {
    return this.http.get(`${this.baseUrl}/k8s/pods`);
  }
  getPrometheusMetrics(): Observable<string> {
    return this.http.get(`${this.baseUrl}/prometheus`, { responseType: 'text' });
  }
  getGrafanaServiceUrl(): Observable<string> {
    return this.http.get(`${this.baseUrl}/grafana`, { responseType: 'text' });
  }

  getGrafanaAdminPassword(): Observable<string> {
    return this.http.get(`${this.baseUrl}/grafanaPwd`, { responseType: 'text' });
  }
}
