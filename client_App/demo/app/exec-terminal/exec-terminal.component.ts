import {AfterViewInit, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import { Terminal } from 'xterm';
import {Subscription} from 'rxjs';
import { ExecServiceService} from './service/exec-service.service';
@Component({
  selector: 'app-exec-terminal',
  templateUrl: './exec-terminal.component.html',
  styleUrls: ['./exec-terminal.component.scss']
})
export class ExecTerminalComponent implements OnInit, AfterViewInit, OnDestroy {

  terminal: Terminal;
  private outputSubscription: Subscription;
  private errorSubscription: Subscription;

  constructor(
    private zone: NgZone,
    // private ExecServiceService: KubernetesDataService

  ) { }

  ngOnInit() {
    // Additional initialization logic if needed
  }

  ngAfterViewInit(): void {
    requestAnimationFrame(() => {
      setTimeout(() => {
        if (!this.terminal) {
          this.initTerminal();
        }
      });
    });
  }

  ngOnDestroy(): void {
    // Cleanup resources when component is destroyed
    if (this.outputSubscription) {
      this.outputSubscription.unsubscribe();
    }
    if (this.errorSubscription) {
      this.errorSubscription.unsubscribe();
    }
    if (this.terminal) {
      this.terminal.dispose(); // Dispose the terminal instance
      const element = document.getElementById('terminal');
      if (element) {
        element.innerHTML = ''; // Remove any associated DOM elements
      }
    }
  }

  public initTerminal(): void {
    this.terminal = new Terminal({
      cursorBlink: true,
      cursorStyle: 'block',
      drawBoldTextInBrightColors: true,
      cursorInactiveStyle: 'underline',
    });

    const element = document.getElementById('terminal');
    if (element) {
      this.terminal.open(element);
      this.fetchCachedSessionData();
      this.setupTerminalInput();
      this.setupTerminalOutput();
      this.setupTerminalError();
      // tslint:disable-next-line:max-line-length
      this.sendKubernetesShellRequest('your-pod', 'your-namespace'); // Replace with actual pod and namespace
    } else {
      console.error('Element not found:', 'terminal');
    }
  }

  private setupTerminalInput(): void {
    this.terminal.onData(data => {
      // Handle terminal input...
      // tslint:disable-next-line:max-line-length
      // this.kubernetesDataService.sendInput(data, 'your-pod', 'your-namespace'); // Replace with actual pod and namespace
    });
  }

  private setupTerminalOutput(): void {
    // tslint:disable-next-line:max-line-length
    /*this.outputSubscription = this.kubernetesDataService.listenForOutput('your-podyour-namespace') // Adjust as needed
      .subscribe(output => {
        console.log('Received terminal output:', output);
        this.zone.run(() => {
          this.terminal.write(output);
        });
      });*/
  }

  private setupTerminalError(): void {
    /*this.errorSubscription = this.kubernetesDataService.listenForError()
      .subscribe(error => {
        this.zone.run(() => {
          this.terminal.writeln(`Error: ${error}`);
        });
      });*/
  }

  private fetchCachedSessionData(): void {
    // tslint:disable-next-line:max-line-length
    /*this.kubernetesDataService.getCachedSessionData('your-namespace', 'your-pod') // Replace with actual pod and namespace
      .subscribe(serializedData => {
        const sessionData = JSON.parse(serializedData);
        console.log('log data: ', serializedData);
        this.terminal.write(sessionData);
      });*/
  }

  private sendKubernetesShellRequest(pod: string, namespace: string): void {
    // this.kubernetesDataService.startShell(namespace, pod);
  }
}
