import { TestBed } from '@angular/core/testing';

import { AdminCockpitService } from './admin-cockpit.service';

describe('AdminCockpitService', () => {
  let service: AdminCockpitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminCockpitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
