import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyAccessRightComponent } from './study-access-right.component';

describe('StudyAccessRightComponent', () => {
  let component: StudyAccessRightComponent;
  let fixture: ComponentFixture<StudyAccessRightComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudyAccessRightComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudyAccessRightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
