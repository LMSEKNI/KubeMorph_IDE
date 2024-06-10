import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Http} from './http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExecServiceService {
  private baseUrl = '/api/runtime';

  constructor(private http: Http) { }
  executeCommand(namespace: string, podName: string, containerName: string, command: string) {
    const requestBody = {
      namespace: namespace,
      podName: podName,
      containerName: containerName,
      command: command
    };
    // @ts-ignore
    return this.http.post<string>(`${this.baseUrl}/podexec`, requestBody, { responseType: 'text' });
  }

  getPodLogs(namespace: string, podName: string): Observable<string> {
    const requestBody = {
      namespace: namespace,
      podName: podName
    };
    return this.http.post<string>(`${this.baseUrl}/podlogs`, requestBody, { responseType: 'text' as 'json' });
  }
}
