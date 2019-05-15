import { TestBed, inject } from '@angular/core/testing';

import { DatasetDesignService } from './dataset-design.service';

describe('DatasetDesignService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DatasetDesignService]
    });
  });

  it('should be created', inject([DatasetDesignService], (service: DatasetDesignService) => {
    expect(service).toBeTruthy();
  }));
});
