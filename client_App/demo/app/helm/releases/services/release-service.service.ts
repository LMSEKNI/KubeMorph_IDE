import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReleaseServiceService {
  private baseUrl = 'http://localhost:8083/api/helm';

  constructor(private http: HttpClient) { }

  listReleases(): Observable<any> {
    return this.http.get(`${this.baseUrl}/list`);
  }

  getReleaseStatus(releaseName: string): Observable<string> {
    // tslint:disable-next-line:max-line-length
    return this.http.get(`${this.baseUrl}/status`, { params: { releaseName }, responseType: 'text' });
  }
  rollbackRelease(releaseName: string, version?: number): Observable<string> {
    let params = new HttpParams().set('releaseName', releaseName);
    if (version !== undefined) {
      params = params.set('version', version.toString());
    }
    return this.http.get(`${this.baseUrl}/rollback-release`, { params, responseType: 'text' });
  }
  deleteRelease(releaseName: string) {
    // tslint:disable-next-line:max-line-length
    return this.http.delete(`${this.baseUrl}/delete-release`, { params: { releaseName }, responseType: 'text' });
  }
}
