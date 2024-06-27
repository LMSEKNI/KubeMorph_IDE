import {Component, Input, OnInit, QueryList, ViewChildren} from '@angular/core';
import {MatTabChangeEvent} from '@angular/material/tabs';
import {ExecTerminalComponent} from '../exec-terminal/exec-terminal.component';

@Component({
  selector: 'app-terminal-tab-manager',
  templateUrl: './terminal-tab-manager.component.html',
  styleUrls: ['./terminal-tab-manager.component.scss']
})
export class TerminalTabManagerComponent  {
  @Input() resourceName: string | null = null;

  terminals: { resourceName: string }[] = [];

  @ViewChildren(ExecTerminalComponent) terminalComponents!: QueryList<ExecTerminalComponent>;

  constructor() {
    // Initialize with one terminal for demo purposes
    this.addTerminal();
  }

  addTerminal(): void {
    const newTerminal = {
      resourceName: `Terminal-${ this.resourceName}`
    };
    this.terminals.push(newTerminal);
  }

  onTabChange(event: MatTabChangeEvent): void {
    // Handle tab change if needed
  }

  executeCommand(terminal: ExecTerminalComponent, command: string): void {
    terminal.executeCommand(command);
  }

}
