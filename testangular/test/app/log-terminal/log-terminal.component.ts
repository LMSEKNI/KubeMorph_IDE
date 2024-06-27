import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Terminal} from 'xterm';
import {Subscription} from 'rxjs';
import {ExecServiceService} from '../exec-terminal/service/exec-service.service';
import {LogService} from './service/log.service';

@Component({
  selector: 'app-log-terminal',
  templateUrl: './log-terminal.component.html',
  styleUrls: ['./log-terminal.component.scss']
})
export class LogTerminalComponent implements OnInit {
  @Input() resourceName: string | null = null;
  @Input() resourceType: string | null = null;

  private terminal: Terminal;
  private commandQueue: string[] = [];
  private commandInProgress = false;
  private outputSubscription: Subscription;

  constructor(private logService: LogService) {

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
    this.getPodLogs();
    //this.terminal.onData(e => this.handleTerminalInput(e)); // Attach listener for user input
  }

  ngOnDestroy(): void {
    this.terminal.dispose();
    if (this.outputSubscription) {
      this.outputSubscription.unsubscribe();
    }
  }

  getPodLogs(): void {
    //const { resourceName } = this.execForm.value;
    console.log("ressourceeeeeeeeeeeLogggggggggggg", this.resourceName);
    this.logService.getPodLogs(this.resourceName).subscribe(
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

  // @ts-ignore

}
