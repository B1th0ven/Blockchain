import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetControlErrorComponent } from './dataset-control-error.component';

describe('DatasetControlErrorComponent', () => {
  let component: DatasetControlErrorComponent;
  let fixture: ComponentFixture<DatasetControlErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetControlErrorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetControlErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
