import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetManagementComponent } from './dataset-management.component';

describe('DatasetManagementComponent', () => {
  let component: DatasetManagementComponent;
  let fixture: ComponentFixture<DatasetManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
