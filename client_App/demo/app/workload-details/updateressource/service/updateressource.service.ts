import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UpdateressourceService {

  private baseUrl = 'http://localhost:8080/api/resources';

  constructor(private http: HttpClient) { }

  // Get resource as JSON
  getResourceAsJson(namespace: string, kind: string, name: string): Observable<object> {
    return this.http.get(`${this.baseUrl}/${namespace}/${kind}/${name}/json`);
  }

  // Get resource as YAML
  getResourceAsYaml(namespace: string, kind: string, name: string): Observable<string> {
    return this.http.get(`${this.baseUrl}/${namespace}/${kind}/${name}/yaml`, { responseType: 'text' });
  }

  updateResource(namespace: string, resourceType: string, resourceName: string, updatedResource: string): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put(`${this.baseUrl}/${namespace}/${resourceType}/${resourceName}`, updatedResource, { headers, responseType: 'text' });
  }
}
