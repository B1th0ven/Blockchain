import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunManagementComponent } from './run-management.component';

describe('RunManagementComponent', () => {
  let component: RunManagementComponent;
  let fixture: ComponentFixture<RunManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
