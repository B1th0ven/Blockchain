import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunDecrementComponent } from './run-decrement.component';

describe('RunDecrementComponent', () => {
  let component: RunDecrementComponent;
  let fixture: ComponentFixture<RunDecrementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunDecrementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunDecrementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
