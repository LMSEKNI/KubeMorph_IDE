import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GrafanaDialogComponent } from './grafana-dialog.component';

describe('GrafanaDialogComponent', () => {
  let component: GrafanaDialogComponent;
  let fixture: ComponentFixture<GrafanaDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GrafanaDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GrafanaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
