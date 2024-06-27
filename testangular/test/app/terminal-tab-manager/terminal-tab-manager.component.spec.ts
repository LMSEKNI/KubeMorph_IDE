import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminalTabManagerComponent } from './terminal-tab-manager.component';

describe('TerminalTabManagerComponent', () => {
  let component: TerminalTabManagerComponent;
  let fixture: ComponentFixture<TerminalTabManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TerminalTabManagerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TerminalTabManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
