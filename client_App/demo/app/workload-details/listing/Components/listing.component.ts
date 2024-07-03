import { AfterViewInit, Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Network } from 'vis-network/peer/esm/vis-network';
import { DataSet } from 'vis-data/peer/esm/vis-data';
import { visOptions } from '../Vis Interface/vis-options.const';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { ListService } from '../Services/list.service';

@Component({
  selector: 'app-kubernetes-visualization',
  templateUrl: './listing.component.html',
  styleUrls: ['./listing.component.scss']
})
export class ListingComponent implements OnInit, AfterViewInit {
  @ViewChild('networkContainer', { static: true }) networkContainer: ElementRef;
  private resources: any[] = [];
  selectedResourceTypes: string[] = ['pods']; // Default to pods
  showResourceOptions = false; // Flag for showing resource options

  private network: Network; // Store the network instance

  constructor(private listService: ListService) {}

  ngOnInit() {
    this.fetchResources();
  }

  ngAfterViewInit() {
    // Ensure the container is fully initialized
    this.initializeNetwork();
  }

  initializeNetwork() {
    const container = this.networkContainer.nativeElement;
    const data = {
      nodes: new DataSet([]),
      edges: new DataSet([])
    };
    this.network = new Network(container, data, visOptions);
  }

  async fetchResources() {
    const nodesArray = [];
    const edgesArray = [];
    let relatedResources: any[];
    const [pods, namespaces] = await Promise.all([
      this.fetchResourceByType('pods'),
      this.fetchResourceByType('namespaces')
    ]);

    for (const resourceType of this.selectedResourceTypes) {
      try {
        const resources = await this.fetchResourceByType(resourceType);

        // Set related resources based on resource type
        switch (resourceType) {
          case 'pods':
            relatedResources = this.resources.filter(r => r.kind === 'namespaces');
            break;
          case 'namespaces':
            relatedResources = this.resources.filter(r => r.kind === 'pods');
            break;
          // Handle other resource types similarly
          default:
            relatedResources = [];
            break;
        }

        nodesArray.push(...this.mapResourcesToNodes(resources, resourceType));

        // Generate edges for the current resource type
        this.generateEdges(edgesArray, pods, 'pods', namespaces);
        this.generateEdges(edgesArray, namespaces, 'namespaces', pods);
        const nodes = new DataSet(nodesArray);
        const edges = new DataSet(edgesArray);
        const data = {
          nodes,
          edges
        };

        // Update the network data
        this.network.setData(data);

        console.log(`Nodes Array after fetching ${resourceType}:`, nodesArray);
        console.log(`Edges Array after generating ${resourceType} edges:`, edgesArray);

      } catch (error) {
        console.error(`Error fetching ${resourceType}:`, error);
      }
    }




  }

  mapResourcesToNodes(resources: any[], resourceType: string): any[] {
    return resources.map(resource => ({
      id: resource.metadata.uid,
      label: resource.metadata.name,
      group: resourceType
    }));
  }

  generateEdges(edges: any[], resources: any[], resourceType: string, relatedResources: any[]) {
    resources.forEach(resource => {
      switch (resourceType) {
        case 'pods':
          this.addEdgesForPod(edges, resource, relatedResources);
          break;
        case 'namespaces':
          this.addEdgesForNamespace(edges, resource, relatedResources);
          break;
        default:
          break;
      }
    });
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

  addEdgesForNamespace(edges: any[], namespace: any, pods: any[]) {
    pods.forEach(pod => {
      if (pod.metadata.namespace === namespace.metadata.name) {
        edges.push({ from: namespace.metadata.uid, to: pod.metadata.uid, arrows: 'to' });
      }
    });
  }

  fetchResourceByType(resourceType: string): Promise<any[]> {
    // Implement fetching logic for each resource type
    switch (resourceType) {
      case 'pods':
        return this.listService.getAllPods().toPromise();
      case 'namespaces':
        return this.listService.getAllNamespaces().toPromise();
      case 'services':
        return this.listService.getAllServices().toPromise();
      case 'deployments':
        return this.listService.getAllDeployments().toPromise();
      case 'replicasets':
        return this.listService.getAllReplicaSets().toPromise();
      case 'jobs':
        return this.listService.getAllJobs().toPromise();
      case 'nodes':
        return this.listService.getAllNodes().toPromise();
      case 'endpoints':
        return this.listService.getAllEndpoints().toPromise();
      case 'configmaps':
        return this.listService.getAllConfigMaps().toPromise();
      case 'ingresses':
        return this.listService.getAllIngress().toPromise();
      case 'persistentvolumeclaims':
        return this.listService.getAllPersistentVolumeClaims().toPromise();
      case 'storageclasses':
        return this.listService.getAllStorageClasses().toPromise();
      case 'persistentvolumes':
        return this.listService.getAllPersistentVolumes().toPromise();
      default:
        return Promise.resolve([]);
    }
  }

  onResourceTypeChange(event: MatCheckboxChange, resourceType: string) {
    if (event.checked) {
      this.selectedResourceTypes.push(resourceType);
    } else {
      this.selectedResourceTypes = this.selectedResourceTypes.filter(type => type !== resourceType);
    }
    this.fetchResources();
  }

  isSelected(resourceType: string): boolean {
    return this.selectedResourceTypes.includes(resourceType);
  }

  toggleResourceOptions() {
    this.showResourceOptions = !this.showResourceOptions;
  }
}
