import { TestBed } from '@angular/core/testing';

import { ReleaseServiceService } from './release-service.service';

describe('ReleaseServiceService', () => {
  let service: ReleaseServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReleaseServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
