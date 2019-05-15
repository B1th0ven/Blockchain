import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpectedCalibrationModalComponent } from './expected-calibration-modal.component';

describe('ExpectedCalibrationModalComponent', () => {
  let component: ExpectedCalibrationModalComponent;
  let fixture: ComponentFixture<ExpectedCalibrationModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExpectedCalibrationModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpectedCalibrationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
