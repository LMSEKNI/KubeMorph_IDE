import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ListtService {

  private baseUrl = 'http://localhost:8080/api/list'; 

  constructor(private http: HttpClient) { }

  
  getAllPods(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/pods`);
  }

  getAllNamespace(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/namespaces`);
  }
  getAllServices(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/services`);
  }
  getAllDeployments(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/deployments`);
  }
  getAllReplicasets(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/replicasets`);
  }
  getAllJobs(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/jobs`);
  }
  getAllNodes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/node`);
  }
  getAllEndpoints(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/endpoints`);
  }
  getAllConfigMaps(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/configmaps`);
  }
  getAllIngress(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/ingress`);
  }
  getAllDeamonsets(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/deamonsets`);
  }
  getAllPVC(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/pvc`);
  }
  getAllSC(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/sc`);
  }
  getAllStateful(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/stateful`);
  }
  getAllPersistentVolumes(): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/persistentvolume`);
  }
}
