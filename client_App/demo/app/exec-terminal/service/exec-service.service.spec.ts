import { TestBed } from '@angular/core/testing';

import { ExecServiceService } from './exec-service.service';

describe('ExecServiceService', () => {
  let service: ExecServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExecServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
