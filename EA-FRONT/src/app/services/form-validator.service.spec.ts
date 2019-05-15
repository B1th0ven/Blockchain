import { TestBed, inject } from '@angular/core/testing';

import { FormValidatorService } from './form-validator.service';

describe('FormValidatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FormValidatorService]
    });
  });

  it('should be created', inject([FormValidatorService], (service: FormValidatorService) => {
    expect(service).toBeTruthy();
  }));
});
