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
  @Input() resource: any;
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
  }

  ngOnDestroy(): void {
    this.terminal.dispose();
    if (this.outputSubscription) {
      this.outputSubscription.unsubscribe();
    }
  }

  getPodLogs(): void {
    console.log('ressourceeeeeeeeeeeLogggggggggggg', this.resource.metadata?.name);
    this.logService.getPodLogs(this.resource.metadata?.name).subscribe(
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
