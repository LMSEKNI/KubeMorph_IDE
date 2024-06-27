import { TestBed } from '@angular/core/testing';

import { DesccService } from './descc.service';

describe('DesccService', () => {
  let service: DesccService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DesccService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
