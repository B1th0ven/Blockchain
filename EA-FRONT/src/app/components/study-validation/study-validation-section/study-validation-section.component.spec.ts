import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyValidationSectionComponent } from './study-validation-section.component';

describe('StudyValidationSectionComponent', () => {
  let component: StudyValidationSectionComponent;
  let fixture: ComponentFixture<StudyValidationSectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyValidationSectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyValidationSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
