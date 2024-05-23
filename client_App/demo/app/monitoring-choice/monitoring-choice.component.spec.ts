import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringChoiceComponent } from './monitoring-choice.component';

describe('MonitoringChoiceComponent', () => {
  let component: MonitoringChoiceComponent;
  let fixture: ComponentFixture<MonitoringChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringChoiceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoringChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
