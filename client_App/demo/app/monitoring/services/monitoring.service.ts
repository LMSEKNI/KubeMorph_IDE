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
  getGrafanaUrl(): Observable<any> {
    return this.http.get(`${this.baseUrl}/grafana-url`, { responseType: 'text' });
  }
  getPrometheusMetrics(): Observable<any> {
    return this.http.get(`${this.baseUrl}/prometheus`);
  }
  getGrafanaServiceUrl(): Observable<string> {
    return this.http.get('http://localhost:8082/api/metrics/grafana', { responseType: 'text' });
  }
}
