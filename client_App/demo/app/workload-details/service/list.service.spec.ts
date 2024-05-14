import { TestBed } from '@angular/core/testing';

import { ListtService } from './list.service';

describe('ListService', () => {
  let service: ListtService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ListtService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
