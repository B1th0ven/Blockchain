import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyDatasetComponent } from './study-dataset.component';

describe('StudyDatasetComponent', () => {
  let component: StudyDatasetComponent;
  let fixture: ComponentFixture<StudyDatasetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyDatasetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyDatasetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
