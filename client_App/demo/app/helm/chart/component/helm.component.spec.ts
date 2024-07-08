import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelmComponent } from './helm.component';

describe('HelmComponent', () => {
  let component: HelmComponent;
  let fixture: ComponentFixture<HelmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HelmComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HelmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
