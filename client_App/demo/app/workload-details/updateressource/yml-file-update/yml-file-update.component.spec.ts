import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YmlFileUpdateComponent } from './yml-file-update.component';

describe('YmlFileUpdateComponent', () => {
  let component: YmlFileUpdateComponent;
  let fixture: ComponentFixture<YmlFileUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YmlFileUpdateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YmlFileUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
