import {AfterViewInit, Component, NgZone, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import { ExecServiceService} from './service/exec-service.service';
import {Terminal} from 'xterm';
import * as xterm from 'xterm';
import {FormBuilder, FormGroup} from '@angular/forms';
@Component({
  selector: 'app-exec-terminal',
  templateUrl: './exec-terminal.component.html',
  styleUrls: ['./exec-terminal.component.scss']
})
export class ExecTerminalComponent implements OnInit, OnDestroy {
  execForm: FormGroup;
  private terminal: Terminal;
  private commandQueue: string[] = [];
  private commandInProgress = false;
  private outputSubscription: Subscription;

  constructor(private fb: FormBuilder, private execService: ExecServiceService) {
    this.execForm = this.fb.group({
      namespace: ['default'],
      podName: ['my-grafana-d49f7477-qxcr5'],
      containerName: ['grafana'],
      command: ['']
    });
  }

  ngOnInit(): void {
    this.terminal = new Terminal({
      cursorBlink: true,
      theme: {
        background: '#000000',
        foreground: '#ffffff'
      }
    });
    this.terminal.open(document.getElementById('terminal'));

    this.terminal.onData(e => this.handleTerminalInput(e)); // Attach listener for user input
  }

  ngOnDestroy(): void {
    this.terminal.dispose();
    if (this.outputSubscription) {
      this.outputSubscription.unsubscribe();
    }
  }

  executeCommand(): void {
    const { namespace, podName, containerName, command } = this.execForm.value;
    this.sendCommand(namespace, podName, containerName, command);
  }

  private handleTerminalInput(data: string): void {
    if (data === '\r') { // Enter key
      const fullCommand = this.commandQueue.join('');
      this.commandQueue = [];
      this.execForm.get('command').setValue(''); // Clear the form command field
      this.sendCommand(
        this.execForm.value.namespace,
        this.execForm.value.podName,
        this.execForm.value.containerName,
        fullCommand
      );
    } else if (data === '\u007F' || data === '\u0008') { // Handle backspace
      if (this.commandQueue.length > 0) {
        this.terminal.write('\b \b'); // Clear the character from terminal visually
        this.commandQueue.pop();
      }
    } else {
      this.terminal.write(data); // Echo the input in the terminal
      this.commandQueue.push(data);
    }
  }

  private sendCommand(namespace: string, podName: string, containerName: string, command: string): void {
    if (this.commandInProgress) {
      this.terminal.write('\r\nCommand in progress. Please wait...\r\n');
      return;
    }
    this.commandInProgress = true;
    this.outputSubscription = this.execService.executeCommand(namespace, podName, containerName, command).subscribe(
      (response) => {
        // @ts-ignore
        const lines = response.split('\n'); // Split response into lines
        lines.forEach(line => {
          this.terminal.write('\r\n'+ line); // Write each line to the terminal
        });

        //this.terminal.write('\n' + response + '\n');
        this.commandInProgress = false;
      },
      (error: any ) => {
        this.terminal.write('\r\nError: ' + error.message + '\r\n');
        this.commandInProgress = false;
      }
    );
  }
  getPodLogs(): void {
    const { namespace, podName } = this.execForm.value;
    this.execService.getPodLogs(namespace, podName).subscribe(
      (response) => {
        const lines = response.split('\n');
        lines.forEach(line => {
          this.terminal.write('\r\n' + line);
        });
      },
      (error) => {
        this.terminal.write('\r\nError: ' + error.message + '\r\n');
      }
    );
  }

}
