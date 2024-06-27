import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Network } from 'vis-network';
import { DataSet } from 'vis-data';
import { ListtService } from '../service/list.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { DesccService } from '../service/descc.service';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DeleteService } from '../service/delete.service';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { DeleteComponent } from '../delete/delete.component';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  animations: [
    trigger('openClose', [
      state('open', style({ transform: 'translateY(0)' })),
      state('closed', style({ transform: 'translateY(100%)' })),
      transition('open <=> closed', [animate('0.3s ease-in-out')]),
    ]),
    trigger('frameAnimation', [
      state('open', style({ width: '50%' })),
      state('closed', style({ width: '0%' })),
      transition('open <=> closed', [animate('0.3s ease-in-out')]),
    ]),
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
export class ListComponent implements OnInit {
  podConnections: any[];
  public network: any;
  public nodes: DataSet<any>;
  public edges: DataSet<any>;
  namespaceNames: string[];
  services: string[];
  podNames: string[];
  deployments: string[];
  replicasets: string[];
  nodess: string[];
  jobs: string[];
  configmaps: string[];
  endpoints: string[];
  ingress: string[];
  deamonsets: string[];
  PVCs: string[];
  SCs: string[];
  statefuls: string[];
  PVs: string[];
  resetOption = true;
  showChecklist = false;
  selectedResources = new FormControl([]);
  resourceList: string[] = [
    'Pods', 'Namespaces', 'Deployments', 'Services', 'ReplicaSets',
    'Jobs', 'Nodes', 'Endpoints', 'ConfigMaps', 'Ingress',
    'DaemonSets', 'PVC', 'SC', 'Stateful', 'PV'
  ];
  isDialogOpen = false;
  @ViewChild('dialogContentTemplate') dialogContentTemplate: TemplateRef<any>;
  constructor(public dialog: MatDialog, private deleteService: DeleteService, private router: Router, private ListService: ListtService, private DescService: DesccService) { }
  resourceDeleted = false;
  alertMessage = '';
  alertType = 'success';
  visible = true;
  selectedResource: string | null = null;
  ressourceName: string[];
  resourceType: string[];
  namespace: string;
  selectedType: string;
  isFrameOpen = false;
  showCheckboxes = false;
  dropdownOpen = false;
  selectedComponent = 'desc';
  selectedChoice: string | null = null;
  showTerminal = false;
  showTerminalContent = false;
  showLogs = false;
  showLogsContent = false;
  isSearchOpen = false;
  toppings = new FormControl('');
  toppingList: string[] = ['Extra cheese', 'Mushroom', 'Onion', 'Pepperoni', 'Sausage', 'Tomato'];

  toggleSearch() {
    this.isSearchOpen = !this.isSearchOpen;
  }

  toggleTerminal(): void {
    this.showTerminal = !this.showTerminal;
    this.showTerminalContent = !this.showTerminalContent;
    if (this.showTerminal) {
      this.showLogs = false;
      this.showLogsContent = false;
    }
    this.closeFrame();
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

  onVisibleChange(eventValue: boolean) {
    this.visible = eventValue;
  }

  openDialog(enterAnimationDuration: string, exitAnimationDuration: string): void {
    this.dialog.open(DeleteComponent, {
      width: '400px',
      enterAnimationDuration,
      exitAnimationDuration,
    });
  }

  onDelete(): void {
    const dialogRef = this.dialog.open(this.dialogContentTemplate, {
      width: '250px'
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        this.deleteressource();
      }
    });
  }

  deleteressource(): void {
    if (this.selectedType && this.selectedResource) {
      this.deleteService.deleteResource(this.selectedType, this.selectedResource)
        .subscribe({
          next: (response) => {
            console.log('Resource deleted successfully:', response);
            this.alertMessage = 'Resource deleted';
            this.alertType = 'success';
            this.dialog.closeAll();
            this.selectedResource = null;
            this.selectedType = null;
            this.isFrameOpen = false;
          },
          error: (error) => {
            console.error('Error deleting resource:', error);
            this.alertMessage = 'Error deleting resource';
            this.alertType = 'danger';
          }
        });
    }
  }

  onclose(): void {
    this.dialog.closeAll();
  }

  renderVisualization(): void {
    const container = document.getElementById('network');
    const data = {
      nodes: this.nodes,
      edges: this.edges
    };
    const options = {};
    this.network = new Network(container, data, options);
    console.log('Nodes:', this.nodes);
  }

  ngOnInit(): void {
    this.nodes = new DataSet<any>();
    this.edges = new DataSet<any>();
    this.selectedResources.valueChanges.subscribe(selected => {
      this.updateNodess(selected);
    });
  }

  onResourceSelected(resourceName: string, resourceType: string): void {
    this.selectedResource = resourceName;
    this.selectedType = resourceType;
  }

  updateNodess(selected: string[]): void {
    this.nodes.clear();
    if (selected.includes('Pods')) {
      this.getAllPodss();
    }
    if (selected.includes('Namespaces')) {
      this.getAllNamespacess();
    }
    if (selected.includes('Deployments')) {
      this.getAllDeploymentss();
    }
    if (selected.includes('Services')) {
      this.getAllServicess();
    }
    if (selected.includes('ReplicaSets')) {
      this.getAllReplicasetss();
    }
    if (selected.includes('Jobs')) {
      this.getAllJobss();
    }
    if (selected.includes('Nodes')) {
      this.getAllNodess();
    }
    if (selected.includes('Endpoints')) {
      this.getAllEndpointss();
    }
    if (selected.includes('ConfigMaps')) {
      this.getAllConfigMapss();
    }
    if (selected.includes('Ingress')) {
      this.getAllIngresss();
    }
    if (selected.includes('DaemonSets')) {
      this.getAllDeamonsetss();
    }
    if (selected.includes('PVC')) {
      this.getAllPVCss();
    }
    if (selected.includes('SC')) {
      this.getAllSCs();
    }
    if (selected.includes('Stateful')) {
      this.getAllStatefuls();
    }
    if (selected.includes('PV')) {
      this.getAllPersistentVolumess();
    }
  }

  getAllPodss(): void {
    this.ListService.getAllPods().subscribe(
      podNames => {
        console.log('Fetched Pods:', podNames);
        this.podNames = podNames;
        this.podNames.forEach((podName, index) => {
          const nodeId = `pod_${index + 1}`;
          const nodeName = `Pod: ${podName}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: podName, shape: 'diamond', color: '#97C2FC' });
        });
        this.renderVisualization();
      },
      error => {
        console.error('Error fetching pods:', error);
      }
    );
  }

  getAllNamespacess(): void {
    this.ListService.getAllNamespace().subscribe(
      namespaceNames => {
        console.log('Fetched Namespaces:', namespaceNames);
        this.namespaceNames = namespaceNames;
        this.namespaceNames.forEach((namespaceName, index) => {
          const nodeId = `namespace_${index + 1}`;
          const nodeName = `Namespace: ${namespaceName}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: namespaceName, shape: 'circle', color: '#FFA500' });
        });
        this.renderVisualization();
      },
      error => {
        console.error('Error fetching namespaces:', error);
      }
    );
  }

  getAllServicess(): void {
    this.ListService.getAllServices().subscribe(
      services => {
        this.services = services;
        this.services.forEach((service, index) => {
          const nodeId = `service_${index + 1}`;
          const nodeName = `service: ${service}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: service, shape: 'triangle', color: '#232d4b' });
        });
        this.renderVisualization();
        console.log('Services:', this.services);
      },
      error => {
        console.error('Error fetching services:', error);
      }
    );
  }

  getAllDeploymentss(): void {
    this.ListService.getAllDeployments().subscribe(
      deployments => {
        this.deployments = deployments;
        this.deployments.forEach((deployment, index) => {
          const nodeId = `deployment_${index + 1}`;
          const nodeName = `deployment: ${deployment}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: deployment, shape: 'hexagon', color: '#CC99FF' });
        });
        this.renderVisualization();
        console.log('deployments:', this.deployments);
      });
  }

  getAllReplicasetss(): void {
    this.ListService.getAllReplicasets().subscribe(
      replicasets => {
        this.replicasets = replicasets;
        this.replicasets.forEach((replicaset, index) => {
          const nodeId = `replicaset_${index + 1}`;
          const nodeName = `replicaset: ${replicaset}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: replicaset, shape: 'hexagon', color: '#f04641' });
        });
        this.renderVisualization();
        console.log('replicasets:', this.replicasets);
      });
  }

  getAllJobss(): void {
    this.ListService.getAllJobs().subscribe(
      jobs => {
        this.jobs = jobs;
        this.jobs.forEach((job, index) => {
          const nodeId = `job_${index + 1}`;
          const nodeName = `job: ${job}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: job, shape: 'hexagon', color: 'green' });
        });
        this.renderVisualization();
        console.log('jobs:', this.jobs);
      });
  }

  getAllNodess(): void {
    this.ListService.getAllNodes().subscribe(
      nodss => {
        this.nodess = nodss;
        this.nodess.forEach((node, index) => {
          const nodeId = `node_${index + 1}`;
          const nodeName = `node: ${node}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: node, shape: 'hexagon', color: 'yellow' });
        });
        this.renderVisualization();
        console.log('Nodes:', this.nodess);
      });
  }

  getAllEndpointss(): void {
    this.ListService.getAllEndpoints().subscribe(
      endpoints => {
        this.endpoints = endpoints;
        this.endpoints.forEach((endpoint, index) => {
          const nodeId = `endpoint_${index + 1}`;
          const nodeName = `endpoint: ${endpoint}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: endpoint, shape: 'hexagon', color: 'red' });
        });
        this.renderVisualization();
        console.log('endpoints:', this.endpoints);
      });
  }

  getAllConfigMapss(): void {
    this.ListService.getAllConfigMaps().subscribe(
      configmaps => {
        this.configmaps = configmaps;
        this.configmaps.forEach((configmap, index) => {
          const nodeId = `configmap_${index + 1}`;
          const nodeName = `configmap: ${configmap}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: configmap, shape: 'hexagon', color: 'black' });
        });
        this.renderVisualization();
        console.log('configmaps:', this.configmaps);
      });
  }

  getAllIngresss(): void {
    this.ListService.getAllIngress().subscribe(
      ingress => {
        this.ingress = ingress;
        this.ingress.forEach((ing, index) => {
          const nodeId = `ingress_${index + 1}`;
          const nodeName = `ingress: ${ing}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: ing, shape: 'hexagon', color: 'black' });
        });
        this.renderVisualization();
        console.log('ingress:', this.ingress);
      });
  }

  getAllDeamonsetss(): void {
    this.ListService.getAllDeamonsets().subscribe(
      deamonsets => {
        this.deamonsets = deamonsets;
        this.deamonsets.forEach((deamonset, index) => {
          const nodeId = `deamonset_${index + 1}`;
          const nodeName = `deamonset: ${deamonset}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: deamonset, shape: 'hexagon', color: '#FF6666' });
        });
        this.renderVisualization();
        console.log('deamonsets:', this.deamonsets);
      });
  }

  getAllPVCss(): void {
    this.ListService.getAllPVC().subscribe(
      PVCs => {
        this.PVCs = PVCs;
        this.PVCs.forEach((PVC, index) => {
          const nodeId = `PVC_${index + 1}`;
          const nodeName = `PVC: ${PVC}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: PVC, shape: 'hexagon', color: '#FF6666' });
        });
        this.renderVisualization();
        console.log('PVCs:', this.PVCs);
      });
  }

  getAllSCs(): void {
    this.ListService.getAllSC().subscribe(
      SCs => {
        this.SCs = SCs;
        this.SCs.forEach((sc, index) => {
          const nodeId = `sc_${index + 1}`;
          const nodeName = `sc: ${sc}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: sc, shape: 'hexagon', color: '#FFCCE5' });
        });
        this.renderVisualization();
        console.log('SCs:', this.SCs);
      });
  }

  getAllStatefuls(): void {
    this.ListService.getAllStateful().subscribe(
      statefuls => {
        this.statefuls = statefuls;
        this.statefuls.forEach((stateful, index) => {
          const nodeId = `stateful_${index + 1}`;
          const nodeName = `stateful: ${stateful}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: stateful, shape: 'hexagon', color: '#660033' });
        });
        this.renderVisualization();
        console.log('statefuls:', this.statefuls);
      });
  }

  getAllPersistentVolumess(): void {
    this.ListService.getAllPersistentVolumes().subscribe(
      PVs => {
        this.PVs = PVs;
        this.PVs.forEach((PV, index) => {
          const nodeId = `persistentvolume_${index + 1}`;
          const nodeName = `persistentvolume: ${PV}`;
          this.nodes.add({ id: nodeId, label: nodeName, title: PV, shape: 'hexagon', color: '#660033' });
        });
        this.renderVisualization();
        console.log('PVs:', this.PVs);
      });
  }

  onNodeClick(properties: any): void {
    const selectedNodes = this.network.getSelectedNodes();
    if (selectedNodes.length === 0) {
      return;
    }
    this.isFrameOpen = true;
    selectedNodes.forEach((nodeId: string) => {
      const node = this.network.body.nodes[nodeId];
      this.selectedResource = node.options.title;
      const labelParts = node.options.label.split(':');
      if (labelParts.length !== 2) {
        console.error('Invalid label format:', node.options.value);
        return;
      }
      const resourceType = labelParts[0].trim().toLowerCase();
      const resourceName = labelParts[1].trim();
      this.selectedType = resourceType;
      console.log('Resource Type:', resourceType);
      console.log('Resource Name:', resourceName);
      this.DescService.getResourceDescriptions(resourceType, resourceName)
        .subscribe(
          (description: string) => {
            console.log('Resource Description:', description);
            this.namespace = this.parseResourceNamespace(description);
            if (this.namespace) {
              console.log('Namespace:', this.namespace);
            } else {
              console.log('Namespace not found in the description');
            }
          },
          (error) => {
            console.error('Error fetching resource description:', error);
          }
        );
    });
  }

  parseResourceNamespace(description: string): string | null {
    const namespacePattern = /Namespace:\s*(\S+)/i;
    const match = description.match(namespacePattern);
    return match ? match[1] : null;
  }

  toggleCheckboxList(): void {
    this.showCheckboxes = !this.showCheckboxes;
  }

  updateEdges(): void {
    this.edges.clear();
    this.podConnections.forEach(connection => {
      this.edges.add({ from: connection.source, to: connection.target });
    });
    this.renderVisualization();
  }

  toggleChecklist() {
    this.showChecklist = !this.showChecklist;
  }
}
