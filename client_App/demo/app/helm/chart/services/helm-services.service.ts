import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HelmServicesService {
  private baseUrl = 'http://localhost:8083/api/helm';

  constructor(private http: HttpClient) { }

  searchRepo(keyword: string): Observable<String[]> {
    const url = `${this.baseUrl}/search`;
    return this.http.get<String[]>(url, { params: { keyword } });
  }
  installChart(
    chartReference: string,
    releaseName: string,
    namespace?: string,
    createNamespace?: boolean,
    dryRun?: boolean
  ): Observable<any> {
    const url = `${this.baseUrl}/installChart`;

    // Setting up query parameters
    let params = new HttpParams()
      .set('chartReference', chartReference)
      .set('releaseName', releaseName);

    // Optional parameters
    if (namespace) {
      params = params.set('namespace', namespace);
    }
    if (createNamespace !== undefined) {
      params = params.set('createNamespace', String(createNamespace));
    }
    if (dryRun !== undefined) {
      params = params.set('dryRun', String(dryRun));
    }

    return this.http.get(url, { params });
  }
  searchHub(keyword: string): Observable<String[]> {
    const url = `${this.baseUrl}/helm-search`;
    return this.http.get<String[]>(url, { params: { keyword } });
  }

}
