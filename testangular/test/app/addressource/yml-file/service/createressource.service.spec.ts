import { TestBed } from '@angular/core/testing';

import { CreateressourceService } from './createressource.service';

describe('CreateressourceService', () => {
  let service: CreateressourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CreateressourceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
