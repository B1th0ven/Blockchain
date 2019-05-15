import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunDecrementTablesComponent } from './run-decrement-tables.component';

describe('RunDecrementTablesComponent', () => {
  let component: RunDecrementTablesComponent;
  let fixture: ComponentFixture<RunDecrementTablesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunDecrementTablesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunDecrementTablesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
