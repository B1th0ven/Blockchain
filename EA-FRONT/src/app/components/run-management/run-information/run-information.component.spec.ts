import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunInformationComponent } from './run-information.component';

describe('RunInformationComponent', () => {
  let component: RunInformationComponent;
  let fixture: ComponentFixture<RunInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
