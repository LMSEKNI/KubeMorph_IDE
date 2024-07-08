import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  NgZone,
  OnDestroy,
  OnInit,
  ViewChild
} from '@angular/core';
import {Subscription} from 'rxjs';
import { ExecServiceService} from './service/exec-service.service';
import {Terminal} from 'xterm';

@Component({
  selector: 'app-exec-terminal',
  templateUrl: './exec-terminal.component.html',
  styleUrls: ['./exec-terminal.component.scss']
})
export class ExecTerminalComponent implements OnInit, OnDestroy,AfterViewInit {
  @Input() resourceType: string | null = null;
  @Input() resourceName: string | null = null;
  @Input() resource: any;
  @ViewChild('terminalElement', { static: true }) terminalElement!: ElementRef;

  protected terminal: Terminal;
  private commandQueue: string[] = [];
  private commandInProgress = false;
  private outputSubscription: Subscription;

  constructor(private execService: ExecServiceService) {
  }
  ngAfterViewInit(): void {
    if (this.terminalElement) {
      this.terminal.open(this.terminalElement.nativeElement);
    }
  }
  ngOnInit(): void {
    this.initializeTerminal();
  }

  private initializeTerminal(): void {
    this.terminal = new Terminal({
      cursorBlink: true,
      theme: {
        background: '#000000',
        foreground: '#ffffff'
      }
    });
    this.displayResourceName();
    this.terminal.onData(e => this.handleTerminalInput(e));
  }

  ngOnDestroy(): void {
    this.terminal.dispose();
    if (this.outputSubscription) {
      this.outputSubscription.unsubscribe();
    }
  }

  executeCommand(command: string): void {
    if (!command) return;
      this.commandQueue.push(command);
      this.sendCommand(this.resource.metadata?.name, command);
  }

  handleTerminalInput(data: string): void {
    if (data === '\r') { // Enter key
      const fullCommand = this.commandQueue.join('');
      this.commandQueue = [];
      this.terminal.write('\r\n'); // Move to a new line
      this.sendCommand(this.resource.metadata?.name, fullCommand);
    } else if (data === '\u007F' || data === '\u0008') {
      if (this.commandQueue.length > 0) {
        this.terminal.write('\b \b');
        this.commandQueue.pop();
      }
    } else {
      this.terminal.write(data);
      this.commandQueue.push(data);
    }
  }

  displayResourceName(): void {
    if (this.resource.metadata?.name) {
      this.terminal.write(`\x1b[32m${this.resource.metadata?.name}\x1b[0m $ `);
    }
  }

  private sendCommand(resourceName: string | null, command: string): void {
    if (!resourceName) {
      this.terminal.write('\r\nResource name is required.\r\n');
      return;
    }
    if (this.commandInProgress) {
      this.terminal.write('\r\nCommand in progress. Please wait...\r\n');
      return;
    }

    console.log(`Sending command: ${command} to resource: ${this.resource.metadata?.name}`);

    this.commandInProgress = true;
    this.outputSubscription = this.execService.executeCommand(this.resource.metadata?.name, command).subscribe(
      (response) => {
        console.log(`Received response: ${response}`);
        //@ts-ignore
        const lines = response.split('\n');
        lines.forEach(line => {
          this.terminal.write('\r\n' + line);
        });
        this.commandInProgress = false;
        this.terminal.write('\r\n');
        this.displayResourceName();
      },
      (error: any) => {
        this.terminal.write('\r\nError: ' + error.message + '\r\n');
        this.commandInProgress = false;
        this.terminal.write('\r\n');  // Move to a new line after error message
        this.displayResourceName();  // Display resource name prompt
      }
    );
  }


}
