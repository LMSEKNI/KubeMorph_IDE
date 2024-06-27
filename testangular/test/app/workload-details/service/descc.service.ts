import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DesccService {

  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getResourceDescriptions(resourceType: string,ressourceName:string): Observable<string> {
    return this.http.get(`${this.baseUrl}/desc/${resourceType}/${ressourceName}`, { responseType: 'text' });
  }
}
