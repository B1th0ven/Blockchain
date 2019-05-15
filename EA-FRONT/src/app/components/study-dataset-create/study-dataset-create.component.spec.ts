import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyDatasetCreateComponent } from './study-dataset-create.component';

describe('StudyDatasetCreateComponent', () => {
  let component: StudyDatasetCreateComponent;
  let fixture: ComponentFixture<StudyDatasetCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyDatasetCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyDatasetCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
