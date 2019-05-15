import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunGeneralComponent } from './run-general.component';

describe('RunGeneralComponent', () => {
  let component: RunGeneralComponent;
  let fixture: ComponentFixture<RunGeneralComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunGeneralComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunGeneralComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
