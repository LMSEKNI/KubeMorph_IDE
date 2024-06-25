import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DynamicFormUpdateComponent } from './dynamic-form-update.component';

describe('DynamicFormUpdateComponent', () => {
  let component: DynamicFormUpdateComponent;
  let fixture: ComponentFixture<DynamicFormUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DynamicFormUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DynamicFormUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
