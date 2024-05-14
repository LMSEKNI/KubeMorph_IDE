import { TestBed } from '@angular/core/testing';

import { CreateressourceformService } from './createressourceform.service';

describe('CreateressourceformService', () => {
  let service: CreateressourceformService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreateressourceformService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
