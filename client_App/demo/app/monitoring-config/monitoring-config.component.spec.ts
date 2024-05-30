import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringConfigComponent } from './monitoring-config.component';

describe('MonitoringConfigComponent', () => {
  let component: MonitoringConfigComponent;
  let fixture: ComponentFixture<MonitoringConfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringConfigComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonitoringConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
