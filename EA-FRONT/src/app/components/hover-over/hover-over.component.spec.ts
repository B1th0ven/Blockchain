import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HoverOverComponent } from './hover-over.component';

describe('HoverOverComponent', () => {
  let component: HoverOverComponent;
  let fixture: ComponentFixture<HoverOverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HoverOverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HoverOverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
