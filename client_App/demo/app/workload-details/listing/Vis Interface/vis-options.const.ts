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
    Pod: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1b3', // server
        color: '#FF5733'
      }
    },
    Node: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf015', // home icon
        color: '#5d2caf'
      }
    },
    Endpoint: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf11c', // endpoints icon
        color: '#5d2caf'
      }
    },
    Daemonset: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0c2', // server
        color: '#01e8f3'
      }
    },
    Service: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf233', // sitemap
        color: '#3498DB'
      },
    },
    Deployment: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1ad', // cogs
        color: '#2ECC71',
      }
    },
    Statefulset: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf013', // cogs
        color: '#d6bc56',
      }
    },
    Namespace: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf24d', // folder-open
        color: '#8E44AD',
      }
    },
    Replicaset: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf085', // gears
        color: '#F39C12',
      }
    },
    Ingresse: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1c0', // sitemap
        color: '#D35400'
      }
    },
    Configmap: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0ae', // tasks
        color: '#9B59B6'
      }
    },
    Storageclasse: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1c0', // database
        color: '#1ABC9C'
      }
    },
    Job: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0c7', // save
        color: '#2980B9'
      }
    },
    Persistentvolume: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd
        color: '#34495E'
      }
    },
    Persistentvolumeclaim: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd
        color: '#00BCD4'
      }
    }
  },
};
