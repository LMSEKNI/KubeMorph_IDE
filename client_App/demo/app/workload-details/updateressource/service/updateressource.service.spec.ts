import { TestBed } from '@angular/core/testing';

import { UpdateressourceService } from './updateressource.service';

describe('UpdateressourceService', () => {
  let service: UpdateressourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpdateressourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
