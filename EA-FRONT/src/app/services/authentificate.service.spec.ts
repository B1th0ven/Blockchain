import { TestBed, inject } from '@angular/core/testing';

import { AuthentificateService } from './authentificate.service';

describe('AuthentificateService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthentificateService]
    });
  });

  it('should be created', inject([AuthentificateService], (service: AuthentificateService) => {
    expect(service).toBeTruthy();
  }));
});
