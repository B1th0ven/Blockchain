import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetControlManagementComponent } from './dataset-control-management.component';

describe('DatasetControlManagementComponent', () => {
  let component: DatasetControlManagementComponent;
  let fixture: ComponentFixture<DatasetControlManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetControlManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetControlManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
