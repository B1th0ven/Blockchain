import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyListSearchComponent } from './study-list-search.component';

describe('StudyListSearchComponent', () => {
  let component: StudyListSearchComponent;
  let fixture: ComponentFixture<StudyListSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyListSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyListSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
