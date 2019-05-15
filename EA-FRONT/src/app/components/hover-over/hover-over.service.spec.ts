import { TestBed, inject } from '@angular/core/testing';

import { HoverOverService } from './hover-over.service';

describe('HoverOverService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HoverOverService]
    });
  });

  it('should be created', inject([HoverOverService], (service: HoverOverService) => {
    expect(service).toBeTruthy();
  }));
});
