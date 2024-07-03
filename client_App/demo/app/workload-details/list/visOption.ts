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
        code: '\uf1b3', // server
        color: '#FF5733'
      }
    },
    services: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf233', // sitemap
        color: '#3498DB'
      },
    },
    deployments: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf013', // cogs
        color: '#2ECC71',
      }
    },
    namespaces: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf24d', // folder-open
        color: '#8E44AD',
      }
    },
    replicasets: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf085', // gears
        color: '#F39C12',
      }
    },
    ingresses: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0e8', // sitemap
        color: '#D35400'
      }
    },
    configmaps: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0ae', // tasks
        color: '#9B59B6'
      }
    },
    storageclasses: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf1c0', // database
        color: '#1ABC9C'
      }
    },
    jobs: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0c7', // save
        color: '#2980B9'
      }
    },
    persistentvolumes: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd
        color: '#34495E'
      }
    },
    persistentvolumeclaims: {
      shape: 'icon',
      icon: {
        face: 'FontAwesome',
        code: '\uf0a0', // hdd
        color: '#00BCD4'
      }
    }
  },
};
