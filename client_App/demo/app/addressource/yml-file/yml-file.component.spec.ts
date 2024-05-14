import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YmlFileComponent } from './yml-file.component';

describe('YmlFileComponent', () => {
  let component: YmlFileComponent;
  let fixture: ComponentFixture<YmlFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YmlFileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YmlFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
