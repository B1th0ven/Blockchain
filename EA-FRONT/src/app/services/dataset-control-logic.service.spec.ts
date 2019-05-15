import { TestBed, inject } from '@angular/core/testing';

import { DatasetControlLogicService } from './dataset-control-logic.service';

describe('DatasetControlLogicService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DatasetControlLogicService]
    });
  });

  it('should be created', inject([DatasetControlLogicService], (service: DatasetControlLogicService) => {
    expect(service).toBeTruthy();
  }));
});
