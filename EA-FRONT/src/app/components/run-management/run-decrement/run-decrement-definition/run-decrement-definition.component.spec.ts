import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunDecrementDefinitionComponent } from './run-decrement-definition.component';

describe('RunDecrementDefinitionComponent', () => {
  let component: RunDecrementDefinitionComponent;
  let fixture: ComponentFixture<RunDecrementDefinitionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunDecrementDefinitionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunDecrementDefinitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
