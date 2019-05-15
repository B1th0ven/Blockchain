import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetControlComponent } from './dataset-control.component';

describe('DatasetControlComponent', () => {
  let component: DatasetControlComponent;
  let fixture: ComponentFixture<DatasetControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetControlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
