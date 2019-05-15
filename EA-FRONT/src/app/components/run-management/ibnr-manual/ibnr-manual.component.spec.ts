import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbnrManualComponent } from './ibnr-manual.component';

describe('IbnrManualComponent', () => {
  let component: IbnrManualComponent;
  let fixture: ComponentFixture<IbnrManualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbnrManualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbnrManualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
