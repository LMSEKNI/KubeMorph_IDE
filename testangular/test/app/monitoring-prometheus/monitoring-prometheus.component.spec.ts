import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringPrometheusComponent } from './monitoring-prometheus.component';

describe('MonitoringPrometheusComponent', () => {
  let component: MonitoringPrometheusComponent;
  let fixture: ComponentFixture<MonitoringPrometheusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringPrometheusComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoringPrometheusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
