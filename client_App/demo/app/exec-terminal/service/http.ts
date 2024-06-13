import { Injectable } from '@angular/core';
import { HttpClient, HttpBackend } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Http extends HttpClient {
  constructor(handler: HttpBackend) {
    super(handler);
  }
}
