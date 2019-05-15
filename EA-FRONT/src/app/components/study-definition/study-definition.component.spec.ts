import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyDefinitionComponent } from './study-definition.component';

describe('StudyDefinitionComponent', () => {
  let component: StudyDefinitionComponent;
  let fixture: ComponentFixture<StudyDefinitionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyDefinitionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyDefinitionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
