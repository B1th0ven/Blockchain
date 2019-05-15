import { TestBed, inject } from '@angular/core/testing';

import { DatasetControlDataService } from './dataset-control-data.service';

describe('DatasetControlDataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DatasetControlDataService]
    });
  });

  it('should be created', inject([DatasetControlDataService], (service: DatasetControlDataService) => {
    expect(service).toBeTruthy();
  }));
});
