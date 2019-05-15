import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatasetDataPrivacyComponent } from './dataset-data-privacy.component';

describe('DatasetDataPrivacyComponent', () => {
  let component: DatasetDataPrivacyComponent;
  let fixture: ComponentFixture<DatasetDataPrivacyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatasetDataPrivacyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatasetDataPrivacyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
