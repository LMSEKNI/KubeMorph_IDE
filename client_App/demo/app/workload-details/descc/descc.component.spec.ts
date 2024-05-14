import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DesccComponent } from './descc.component';

describe('DesccComponent', () => {
  let component: DesccComponent;
  let fixture: ComponentFixture<DesccComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DesccComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DesccComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
