import { resourceGroups } from './groupe-ressource';
import { Options } from 'vis-network/peer';

export const visOptions: Options = {
  autoResize: true,
  height: '100%',
  width: '100%',
  locale: 'en',
  manipulation: {
    addNode: function (nodeData, callback) {
      nodeData.label = 'hello world';
      callback(nodeData);
    }
  },
  physics: {
    enabled: true,
    barnesHut: {
      gravitationalConstant: -4000,
      centralGravity: 0.3,
      springLength: 100,
      springConstant: 0.09,
      damping: 0.09,
      avoidOverlap: 0
    },
    minVelocity: 0.75,
    solver: 'barnesHut',
    adaptiveTimestep: true
  },
  layout: {
    randomSeed: undefined,
    improvedLayout: true,
    hierarchical: {
      enabled: false
    }
  },
  nodes: {
    borderWidth: 2,
    size: 30,
    color: {
      border: '#222222',
      background: '#666666'
    },
    font: {
      color: '#312c2c'
    },
    shape: 'dot'
  },
  edges: {
    color: 'gray',
    smooth: {
      enabled: true,
      type: 'curvedCW',
      roundness: 0.1
    }
  },
  interaction: {
    tooltipDelay: 200,
    hideEdgesOnDrag: false,
    hideNodesOnDrag: false,
    hover: true,
    multiselect: true,
    navigationButtons: true,
    selectable: true,
    zoomSpeed: 0.2,
    zoomView: true,
    dragNodes: true
  },
  groups: resourceGroups
};
