import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  V1Pod, V1Namespace, V1Service, V1Deployment, V1ReplicaSet, V1Job,
  V1Node, V1ConfigMap, V1Ingress, V1Endpoints, V1DaemonSet, V1PersistentVolumeClaim,
  V1PersistentVolume, V1StorageClass, V1StatefulSet
} from '@kubernetes/client-node';

@Injectable({
  providedIn: 'root'
})
export class ListService {

  private baseUrl = 'http://localhost:8080/api/list';

  constructor(private http: HttpClient) { }

  getAllPods(): Observable<V1Pod[]> {
    return this.http.get<V1Pod[]>(`${this.baseUrl}/pods`);
  }

  getAllNamespaces(): Observable<V1Namespace[]> {
    return this.http.get<V1Namespace[]>(`${this.baseUrl}/namespaces`);
  }

  getAllServices(): Observable<V1Service[]> {
    return this.http.get<V1Service[]>(`${this.baseUrl}/services`);
  }

  getAllDeployments(): Observable<V1Deployment[]> {
    return this.http.get<V1Deployment[]>(`${this.baseUrl}/deployments`);
  }

  getAllReplicaSets(): Observable<V1ReplicaSet[]> {
    return this.http.get<V1ReplicaSet[]>(`${this.baseUrl}/replicasets`);
  }

  getAllJobs(): Observable<V1Job[]> {
    return this.http.get<V1Job[]>(`${this.baseUrl}/jobs`);
  }

  getAllNodes(): Observable<V1Node[]> {
    return this.http.get<V1Node[]>(`${this.baseUrl}/nodes`);
  }

  getAllEndpoints(): Observable<V1Endpoints[]> {
    return this.http.get<V1Endpoints[]>(`${this.baseUrl}/endpoints`);
  }

  getAllConfigMaps(): Observable<V1ConfigMap[]> {
    return this.http.get<V1ConfigMap[]>(`${this.baseUrl}/configmaps`);
  }

  getAllIngress(): Observable<V1Ingress[]> {
    return this.http.get<V1Ingress[]>(`${this.baseUrl}/ingresses`);
  }

  getAllDaemonSets(): Observable<V1DaemonSet[]> {
    return this.http.get<V1DaemonSet[]>(`${this.baseUrl}/daemonsets`);
  }

  getAllPersistentVolumeClaims(): Observable<V1PersistentVolumeClaim[]> {
    return this.http.get<V1PersistentVolumeClaim[]>(`${this.baseUrl}/persistentvolumeclaims`);
  }

  getAllStorageClasses(): Observable<V1StorageClass[]> {
    return this.http.get<V1StorageClass[]>(`${this.baseUrl}/storageclasses`);
  }

  getAllStatefulSets(): Observable<V1StatefulSet[]> {
    return this.http.get<V1StatefulSet[]>(`${this.baseUrl}/statefulsets`);
  }

  getAllPersistentVolumes(): Observable<V1PersistentVolume[]> {
    return this.http.get<V1PersistentVolume[]>(`${this.baseUrl}/persistentvolumes`);
  }
}
