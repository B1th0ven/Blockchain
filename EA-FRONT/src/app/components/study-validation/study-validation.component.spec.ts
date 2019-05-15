import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyValidationComponent } from './study-validation.component';

describe('StudyValidationComponent', () => {
  let component: StudyValidationComponent;
  let fixture: ComponentFixture<StudyValidationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyValidationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyValidationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
