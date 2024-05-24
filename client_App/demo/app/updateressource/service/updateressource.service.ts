import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UpdateressourceService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getRessourceUpdate(namespace: string, podName: string): Observable<object> {
    return this.http.get(`${this.baseUrl}/update/${namespace}/${podName}`);
  }
  updateResource(resourceType: string, resourceName: string, updatedResource: object): Observable<object> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.baseUrl}/update/${resourceType}/${resourceName}`, updatedResource, { headers });
  }
}
