import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteReleaseComponent } from './delete-release.component';

describe('DeleteReleaseComponent', () => {
  let component: DeleteReleaseComponent;
  let fixture: ComponentFixture<DeleteReleaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteReleaseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteReleaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
