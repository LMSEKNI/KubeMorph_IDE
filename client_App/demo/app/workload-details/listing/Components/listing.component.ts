import { AfterViewInit, Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Network } from 'vis-network/peer/esm/vis-network';
import { DataSet } from 'vis-data/peer/esm/vis-data';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { ListService } from '../Services/list.service';
import { Router } from '@angular/router';
import {visOptions} from '../../list/visOption';
import {FormControl} from '@angular/forms';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {DeleteComponent} from '../../delete/delete.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-kubernetes-visualization',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.scss'],
  animations : [
    trigger('toggleSearch', [
      state('open', style({
        width: '200px',
        opacity: 1,
        visibility: 'visible'
      })),
      state('closed', style({
        width: '0',
        opacity: 0,
        visibility: 'hidden'
      })),
      transition('closed => open', [
        animate('0.3s ease-in-out')
      ]),
      transition('open => closed', [
        animate('0.3s ease-in-out')
      ])
    ])
  ]
})
export class ListingComponent implements OnInit, AfterViewInit {
  @ViewChild('networkContainer', { static: true }) networkContainer: ElementRef;
  private resourcesByUid: Map<string, any> = new Map<string, any>();
  private network: Network;
  private nodes: DataSet<any>;
  selectedResourceTypes: string[] = [];
  showResourceOptions = false;
  selectedResources = new FormControl([]);
  isSearchOpen = false;
  isFrameOpen = false;
  selectedComponent = 'desc';
  showRightPanel = false;
  dropdownOpen = false;
  isDialogOpen = false;
  public showTerminal = false;
  public showTerminalContent = false;
  public showLogs = false;
  public showLogsContent = false;
  showVerticalSplitter = false;
  selectedResource: any;

  constructor(private listService: ListService,
              private router: Router,
              public dialog: MatDialog) {}

  ngOnInit(): void {
    this.selectedResources.valueChanges.subscribe(selected => {
      this.selectedResourceTypes = selected;  // Update selected resource types
      this.fetchResources();
    });

  }
  toggleTerminal(): void {
    this.showVerticalSplitter = !this.showVerticalSplitter;
    this.showTerminal = !this.showTerminal;
    this.showTerminalContent = !this.showTerminalContent;
    if (this.showTerminal) {
      this.showLogs = false;
      this.showLogsContent = false;
    }
    // this.closeFrame();
  }

  toggleLog(): void {
    this.showLogs = !this.showLogs;
    this.showLogsContent = !this.showLogsContent;
    if (this.showLogs) {
      this.showTerminal = false;
      this.showTerminalContent = false;
    }
    this.closeFrame();
  }
  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }
  selectOption(option: string): void {
    this.dropdownOpen = false;
    if (option === 'form') {
      this.selectedComponent = 'form';
    } else if (option === 'yaml') {
      this.selectedComponent = 'yaml';
    }
  }
  closeFrame(): void {
    this.isFrameOpen = false;
    this.selectedComponent = 'desc';
  }

  openDialog(enterAnimationDuration: string, exitAnimationDuration: string): void {
    this.dialog.open(DeleteComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  openDeleteDialog(resourceType: string, resource: any): void {
    const dialogRef = this.dialog.open(DeleteComponent, {
      width: '250px',
      data: { resourceType, resource }
    });

  }
  closeTerminal(): void {
    this.showTerminal = false;
    this.showTerminalContent = false;

  }
  closeLogs(): void {
    this.showLogs = false;
    this.showLogsContent = false;
  }
  toggleSearch() {
    this.isSearchOpen = !this.isSearchOpen;
  }

  ngAfterViewInit(): void {
    this.initializeNetwork();
  }

  initializeNetwork(): void {
    const container = this.networkContainer.nativeElement;
    this.nodes = new DataSet([]);
    const data = {
      nodes: this.nodes,
      edges: new DataSet([])
    };
    this.network = new Network(container, data, visOptions);

    this.network.on('click', (params) => {
      if (params.nodes.length > 0) {
        const nodeId = params.nodes[0];
        console.log(`Node id :`, nodeId);
        const resource = this.resourcesByUid.get(nodeId);
        console.log('resourrrrrrrrrrr', resource);
        if (resource) {
          this.selectedResource = resource;
          this.showRightPanel = true;
          this.selectedComponent = 'desc';        }
      }
    });
  }

  async fetchResources() {
    const nodesArray = [];
    const edgesArray = [];
    const [Pod, Namespace, Deployment, Service, Daemonset, Job, Node, Replicaset, Statefulset, Configmap, Ingresse, Persistentvolumeclaim, Persistentvolume, Endpoint ] = await Promise.all([
      this.fetchResourceByType('Pod'),
      this.fetchResourceByType('Namespace'),
      this.fetchResourceByType('Deployment'),
      this.fetchResourceByType('Service'),
      this.fetchResourceByType('Replicaset'),
      this.fetchResourceByType('Statefulset'),
      this.fetchResourceByType('Configmap'),
      this.fetchResourceByType('Ingresse'),
      this.fetchResourceByType('Persistentvolumeclaim'),
      this.fetchResourceByType('Persistentvolume'),
      this.fetchResourceByType('Endpoint'),
      this.fetchResourceByType('Daemonset'),
      this.fetchResourceByType('Job'),
      this.fetchResourceByType('Node'),
    ]);
    // Store resources in local map by UID
    this.storeResources(Pod);
    this.storeResources(Namespace);
    this.storeResources(Deployment);
    this.storeResources(Service);
    this.storeResources(Replicaset);
    this.storeResources(Statefulset);
    this.storeResources(Configmap);
    this.storeResources(Ingresse);
    this.storeResources(Persistentvolumeclaim);
    this.storeResources(Persistentvolume);
    this.storeResources(Endpoint);
    this.storeResources(Daemonset);
    this.storeResources(Job);
    this.storeResources(Node);


    for (const resourceType of this.selectedResourceTypes) {
      try {
        const resources = await this.fetchResourceByType(resourceType);

        nodesArray.push(...this.mapResourcesToNodes(resources, resourceType));

        this.generateEdges(edgesArray, Pod, 'Pod', Namespace);
        this.generateEdges(edgesArray, Deployment, 'Deployment', Namespace);
        this.generateEdges(edgesArray, Service, 'Service', Namespace);
        this.generateEdges(edgesArray, Replicaset, 'Replicaset', Namespace);
        this.generateEdges(edgesArray, Replicaset, 'Replicaset', Namespace);
        this.generateEdges(edgesArray, Statefulset, 'Statefulset', Namespace);
        this.generateEdges(edgesArray, Configmap, 'Configmap', Namespace);
        this.generateEdges(edgesArray, Ingresse, 'Ingresse', Namespace);
        this.generateEdges(edgesArray, Persistentvolumeclaim, 'Persistentvolumeclaim', Namespace);
        this.generateEdges(edgesArray, Persistentvolumeclaim, 'Persistentvolume', Namespace);
        this.generateEdges(edgesArray, Endpoint, 'Endpoint', Namespace);
        this.generateEdges(edgesArray, Daemonset, 'Daemonset', Namespace);
        this.generateEdges(edgesArray, Job, 'Job', Namespace);
        this.generateEdges(edgesArray, Node, 'Node', Namespace);
        const nodes = new DataSet(nodesArray);
        const edges = new DataSet(edgesArray);
        const data = {
          nodes,
          edges
        };

        this.network.setData(data);
        console.log(`Nodes Array after fetching ${resourceType}:`, nodesArray);
        console.log(`Edges Array after generating ${resourceType} edges:`, edgesArray);

      } catch (error) {
        console.error(`Error fetching ${resourceType}:`, error);
      }
    }
  }
  storeResources(resources: any[]): void {
    resources.forEach(resource => {
      this.resourcesByUid.set(resource.metadata.uid, resource);
    });
  }

  mapResourcesToNodes(resources: any[], resourceType: string): any[] {
    console.log('Resource:', resources); // Check resource structure

    return resources.map(resource => ({
      id: resource.metadata.uid,
      label: resource.metadata.name,
      group: resourceType
    }));
  }

  generateEdges(edges: any[], resources: any[], resourceType: string, relatedResources: any[]) {
    resources.forEach(resource => {
      switch (resourceType) {
        case 'Pod':
          this.addEdgesForPod(edges, resource, relatedResources);
          break;
        case 'Deployment':
          this.addEdgesForDeployment(edges, resource, relatedResources);
          break;
        case 'Service':
          this.addEdgesForServices(edges, resource, relatedResources);
          break;
        case 'Replicaset':
          this.addEdgesForReplicasets(edges, resource, relatedResources);
          break;
        case 'Configmap':
          this.addEdgesForConfigmaps(edges, resource, relatedResources);
          break;
        case 'Ingresse':
          this.addEdgesForIngresses(edges, resource, relatedResources);
          break;
        case 'Statefulset':
          this.addEdgesForStatefulsets(edges, resource, relatedResources);
          break;
        case 'Persistentvolumeclaim':
          this.addEdgesForPersistentvolumeclaims(edges, resource, relatedResources);
          break;
        case 'Persistentvolume':
          this.addEdgesForPersistentvolume(edges, resource, relatedResources);
          break;
        case 'Endpoint':
          this.addEdgesForEndpoints(edges, resource, relatedResources);
          break;
        case 'Daemonset':
          this.addEdgesForDaemonset(edges, resource, relatedResources);
          break;
        case 'Job':
          this.addEdgesForJob(edges, resource, relatedResources);
          break;
        case 'Node':
          this.addEdgesForNode(edges, resource, relatedResources);
          break;
        default:
          break;
      }
    });
  }
  addEdgesForEndpoints(edges: any[], endpoint: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === endpoint.metadata.namespace);
    if (namespace) {
      edges.push({ from: endpoint.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }
    if (endpoint.spec && endpoint.spec.serviceName) {
      edges.push({ from: endpoint.metadata.uid, to: endpoint.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForJob(edges: any[], job: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === job.metadata.namespace);
    if (namespace) {
      edges.push({ from: job.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }
    if (job.spec && job.spec.serviceName) {
      edges.push({ from: job.metadata.uid, to: job.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForPersistentvolumeclaims(edges: any[], persistentvolumeclaim: any, namespaces: any[]) {
    // tslint:disable-next-line:max-line-length
    const namespace = namespaces.find(ns => ns.metadata.name === persistentvolumeclaim.metadata.namespace);
    if (namespace) {
      edges.push({ from: persistentvolumeclaim.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }
    if (persistentvolumeclaim.spec && persistentvolumeclaim.spec.serviceName) {
      edges.push({ from: persistentvolumeclaim.metadata.uid, to: persistentvolumeclaim.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForPersistentvolume(edges: any[], persistentvolume: any, namespaces: any[]) {
    // tslint:disable-next-line:max-line-length
    const namespace = namespaces.find(ns => ns.metadata.name === persistentvolume.metadata.namespace);
    if (namespace) {
      edges.push({ from: persistentvolume.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }
    if (persistentvolume.spec && persistentvolume.spec.serviceName) {
      edges.push({ from: persistentvolume.metadata.uid, to: persistentvolume.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForIngresses(edges: any[], ingresse: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === ingresse.metadata.namespace);
    if (namespace) {
      edges.push({ from: ingresse.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (ingresse.spec && ingresse.spec.serviceName) {
      edges.push({ from: ingresse.metadata.uid, to: ingresse.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForNode(edges: any[], node: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === node.metadata.namespace);
    if (namespace) {
      edges.push({ from: node.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }
    if (node.spec && node.spec.serviceName) {
      edges.push({ from: node.metadata.uid, to: node.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForConfigmaps(edges: any[], configmap: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === configmap.metadata.namespace);
    if (namespace) {
      edges.push({ from: configmap.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (configmap.spec && configmap.spec.serviceName) {
      edges.push({ from: configmap.metadata.uid, to: configmap.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForPod(edges: any[], pod: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === pod.metadata.namespace);
    if (namespace) {
      edges.push({ from: pod.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (pod.spec && pod.spec.serviceName) {
      edges.push({ from: pod.metadata.uid, to: pod.spec.serviceName, arrows: 'to' });
    }
  }

  addEdgesForDaemonset(edges: any[], Daemonset: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === Daemonset.metadata.namespace);
    if (namespace) {
      edges.push({ from: Daemonset.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (Daemonset.spec && Daemonset.spec.serviceName) {
      edges.push({ from: Daemonset.metadata.uid, to: Daemonset.spec.serviceName, arrows: 'to' });
    }
  }

  addEdgesForDeployment(edges: any[], deployment: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === deployment.metadata.namespace);
    if (namespace) {
      edges.push({ from: deployment.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (deployment.spec && deployment.spec.serviceName) {
      edges.push({ from: deployment.metadata.uid, to: deployment.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForServices(edges: any[], service: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === service.metadata.namespace);
    if (namespace) {
      edges.push({ from: service.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (service.spec && service.spec.serviceName) {
      edges.push({ from: service.metadata.uid, to: service.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForReplicasets(edges: any[], replicaset: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === replicaset.metadata.namespace);
    if (namespace) {
      edges.push({ from: replicaset.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (replicaset.spec && replicaset.spec.serviceName) {
      edges.push({ from: replicaset.metadata.uid, to: replicaset.spec.serviceName, arrows: 'to' });
    }
  }
  addEdgesForStatefulsets(edges: any[], statefulset: any, namespaces: any[]) {
    const namespace = namespaces.find(ns => ns.metadata.name === statefulset.metadata.namespace);
    if (namespace) {
      edges.push({ from: statefulset.metadata.uid, to: namespace.metadata.uid, arrows: 'to' });
    }

    if (statefulset.spec && statefulset.spec.serviceName) {
      edges.push({ from: statefulset.metadata.uid, to: statefulset.spec.serviceName, arrows: 'to' });
    }
  }
  fetchResourceByType(selectedResources: string): Promise<any[]> {
    // Implement fetching logic for each resource type
    switch (selectedResources) {
      case 'Pod':
        return this.listService.getAllPods().toPromise();
      case 'Namespace':
        return this.listService.getAllNamespaces().toPromise();
      case 'Service':
        return this.listService.getAllServices().toPromise();
      case 'Deployment':
        return this.listService.getAllDeployments().toPromise();
      case 'Replicaset':
        return this.listService.getAllReplicaSets().toPromise();
      case 'Job':
        return this.listService.getAllJobs().toPromise();
      case 'Node':
        return this.listService.getAllNodes().toPromise();
      case 'Endpoint':
        return this.listService.getAllEndpoints().toPromise();
      case 'Configmap':
        return this.listService.getAllConfigMaps().toPromise();
      case 'Ingresse':
        return this.listService.getAllIngress().toPromise();
      case 'Persistentvolumeclaim':
        return this.listService.getAllPersistentVolumeClaims().toPromise();
      case 'Storageclasse':
        return this.listService.getAllStorageClasses().toPromise();
      case 'Persistentvolume':
        return this.listService.getAllPersistentVolumes().toPromise();
      case 'Statefulset':
        return this.listService.getAllStatefulSets().toPromise();
      case 'Daemonset':
        return this.listService.getAllDaemonSets().toPromise();
      default:
        return Promise.resolve([]);
    }
  }

  onResourceTypeChange(event: MatCheckboxChange, selectedResources: string) {
    if (event.checked) {
      this.selectedResourceTypes.push(selectedResources);
    } else {
      this.selectedResourceTypes = this.selectedResourceTypes.filter(type => type !== selectedResources);
    }
    this.fetchResources();
  }

  isSelected(selectedResources: string): boolean {
    return this.selectedResourceTypes.includes(selectedResources);
  }

  toggleResourceOptions() {
    this.showResourceOptions = !this.showResourceOptions;
  }
}
