import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExecTerminalComponent } from './exec-terminal.component';

describe('ExecTerminalComponent', () => {
  let component: ExecTerminalComponent;
  let fixture: ComponentFixture<ExecTerminalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExecTerminalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExecTerminalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
