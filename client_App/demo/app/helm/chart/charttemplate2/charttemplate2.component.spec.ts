import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Charttemplate2Component } from './charttemplate2.component';

describe('Charttemplate2Component', () => {
  let component: Charttemplate2Component;
  let fixture: ComponentFixture<Charttemplate2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Charttemplate2Component ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Charttemplate2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
