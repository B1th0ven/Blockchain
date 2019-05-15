import { TestBed, inject } from '@angular/core/testing';

import { IbnrControlService } from './ibnr-control.service';

describe('IbnrControlService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IbnrControlService]
    });
  });

  it('should be created', inject([IbnrControlService], (service: IbnrControlService) => {
    expect(service).toBeTruthy();
  }));
});
