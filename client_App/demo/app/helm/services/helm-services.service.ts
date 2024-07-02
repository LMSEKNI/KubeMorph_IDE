import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
  searchHub(keyword: string): Observable<String[]> {
    const url = `${this.baseUrl}/helm-search`;
    return this.http.get<String[]>(url, { params: { keyword } });
  }

}
