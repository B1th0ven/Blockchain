import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunDecTabDefComponent } from './run-dec-tab-def.component';

describe('RunDecTabDefComponent', () => {
  let component: RunDecTabDefComponent;
  let fixture: ComponentFixture<RunDecTabDefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunDecTabDefComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunDecTabDefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
