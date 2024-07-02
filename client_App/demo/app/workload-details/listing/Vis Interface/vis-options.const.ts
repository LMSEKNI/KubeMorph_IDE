import { Options } from 'vis';

export const visOptions: Options = {
  autoResize: true,
  height: '100%',
  width: '100%',
  layout: {
    hierarchical: {
      enabled: false,
      sortMethod: 'directed',
      direction: 'UD', // Up-Down, other options: LR, RL, DU
      levelSeparation: 150,
      nodeSpacing: 200,
      treeSpacing: 200,
    },
  },
  nodes: {
    shape: 'dot',
    size: 30,
    font: {
      size: 20,
      face: 'Tahoma',
    },
    borderWidth: 2,
  },
  edges: {
    arrows: {
      to: { enabled: true, scaleFactor: 0.5 } // Show arrows at the end of edges
    },
    color: {
      color: '#005573',
      highlight: '#FF0000'
    },
    width: 2,
    smooth: {
      enabled: true,
      type: 'continuous',
      roundness: 0.5,
    },
  },
  physics: {
    enabled: true,
    barnesHut: {
      gravitationalConstant: -30000,
      springConstant: 0.04,
      springLength: 95,
    },
  },
  interaction: {
    dragNodes: true,
    dragView: true,
    hover: true,
    zoomView: true,
  },
  groups: {
    pods: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1b3', // cube icon, representing a Pod
        color: '#FF9900'
      }
    },
    services: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0c0', // users icon, symbolically representing a Service
        color: '#2B7CE9'
      },
    },
    deployments: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf013', // cog icon, representing a Deployment
        color: '#4CAF50',
      }
    },
    namespaces: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf013', // cog icon, representing a Deployment
        color: '#4a348a',
      }
    },
    Ingress: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0e8', // random icon, as an example to represent an Ingress
        color: '#795548'
      }
    },
    configmaps: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf24d', // file-code-o icon, for ConfigMaps
        color: '#9C27B0'
      }
    },
    Secret: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf084', // key icon, representing a Secret
        color: '#009688'
      }
    },
    Node: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf233', // server icon, for Nodes
        color: '#3F51B5'
      }
    },
    PersistentVolume: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd icon, symbolically representing a PersistentVolume
        color: '#607D8B'
      }
    },
    PersistentVolumeClaim: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd icon, can also represent a PersistentVolumeClaim
        color: '#00BCD4'
      }
    }
  },
};
