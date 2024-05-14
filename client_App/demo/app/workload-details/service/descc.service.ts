import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DesccService {

  private baseUrl = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) { }

 
  // getPodDescriptions(podName: string): Observable<string> {
  //   return this.http.get(`${this.baseUrl}/desc/poddesc/${podName}`, { responseType: 'text' });
  // }
  // getServiceDescriptions(serviceName: string): Observable<string> {
  //   return this.http.get(`${this.baseUrl}/desc/servdesc/${serviceName}`, { responseType: 'text' });
  // }
  getResourceDescriptions(resourceType: string,ressourceName :string): Observable<string> {
    return this.http.get(`${this.baseUrl}/desc/${resourceType}/${ressourceName}`, { responseType: 'text' });
  }
}
