import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CreateressourceformService {
  private apiUrl = 'http://localhost:8080/api/create/resource/form';
 
  constructor(private http: HttpClient) { }
 
  createResource(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }
}
