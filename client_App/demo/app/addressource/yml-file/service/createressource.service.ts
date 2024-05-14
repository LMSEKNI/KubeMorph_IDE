import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CreateressourceService {

  constructor(private http: HttpClient) { }
  private apiUrl = 'http://localhost:8080/api/create/resource'; 

  addResource(formData: FormData) {
    return this.http.post(`${this.apiUrl}`, formData);
  }
}
