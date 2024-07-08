import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CharttemplateComponent } from './charttemplate.component';

describe('CharttemplateComponent', () => {
  let component: CharttemplateComponent;
  let fixture: ComponentFixture<CharttemplateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CharttemplateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CharttemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
