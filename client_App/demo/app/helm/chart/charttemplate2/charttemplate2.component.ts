import { Component, OnInit } from '@angular/core';
import {range} from 'rxjs';

@Component({
  selector: 'app-charttemplate2',
  templateUrl: './charttemplate2.component.html',
  styleUrls: ['./charttemplate2.component.scss']
})
export class Charttemplate2Component implements OnInit {

  code1 = `
1  apiVersion: apps/v1
2  kind: Deployment
3  metadata:
4    name: {{ .Values.Name }}
5  spec:
6    replicas: {{ .Values.replicas }}
7    selector:
8       matchLabels:
9          {{- range $key, $val := .Values.deploymentlabels }}
10         {{ $key }}: {{ $val | quote }}
11         {{- end }}
12   template:
13     metadata:
14       name: {{ .Values.Name }}
15       labels:
16         {{- range $key, $val := .Values.deploymentlabels }}
17         {{ $key }}: {{ $val | quote }}
18         {{- end }}
19     spec:
20       containers:
21       - name: {{ .Values.Name }}
22         image: {{ .Values.image }}
23         ports:
24         - containerPort: {{ .Values.ports.containerport }}
  `;

  code2 = `
1 apiVersion: v1
2 kind: Service
3 metadata:
4  name: {{ .Values.Name }}-svc
5 spec:
6   type: {{ .Values.servicetype }}
7   selector:
8      {{- range $key, $val := .Values.deploymentlabels }}
9      {{ $key }}: {{ $val | quote }}
10      {{- end }}
11   ports:
12    - protocol: TCP
13      port: {{ .Values.ports.serviceport }}
14      targetPort: {{ .Values.ports.containerport }}
  `;

  code = this.code1;

  showCode(codeKey: string) {
    this.code = codeKey === 'code1' ? this.code1 : this.code2;
  }

  copyCode() {
    navigator.clipboard.writeText(this.code);
    alert('Code copied to clipboard!');
  }

  ngOnInit(): void {
  }
}
