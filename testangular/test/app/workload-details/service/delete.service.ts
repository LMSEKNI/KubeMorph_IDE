import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DeleteService {

  private baseUrl = 'http://localhost:8080/api'; 

  constructor(private http: HttpClient) { }
  
  deleteResource(resourceType: string, resourceName: string) {
    return this.http.delete(`${this.baseUrl}/delete/${resourceType}/${resourceName}`);
  }

}
