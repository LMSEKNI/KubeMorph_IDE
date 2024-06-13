import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {Http} from '../../exec-terminal/service/http';

@Injectable({
  providedIn: 'root'
})
export class LogService {
  private baseUrl = '/api/runtime';

  constructor(private http: Http) { }
  getPodLogs(podName: string): Observable<string> {
    const requestBody = {
      podName: podName
    };
    return this.http.post<string>(`${this.baseUrl}/podlogs`, requestBody, { responseType: 'text' as 'json' });
  }
}
