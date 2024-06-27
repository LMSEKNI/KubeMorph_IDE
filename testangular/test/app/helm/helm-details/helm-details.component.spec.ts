import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelmDetailsComponent } from './helm-details.component';

describe('HelmDetailsComponent', () => {
  let component: HelmDetailsComponent;
  let fixture: ComponentFixture<HelmDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HelmDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HelmDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
