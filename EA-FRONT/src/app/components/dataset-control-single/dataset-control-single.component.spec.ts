import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetControlSingleComponent } from './dataset-control-single.component';

describe('DatasetControlSingleComponent', () => {
  let component: DatasetControlSingleComponent;
  let fixture: ComponentFixture<DatasetControlSingleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetControlSingleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetControlSingleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
