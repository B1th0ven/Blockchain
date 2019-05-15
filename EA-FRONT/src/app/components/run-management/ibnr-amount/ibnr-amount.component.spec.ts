import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IbnrAmountComponent } from './ibnr-amount.component';

describe('IbnrAmountComponent', () => {
  let component: IbnrAmountComponent;
  let fixture: ComponentFixture<IbnrAmountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IbnrAmountComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IbnrAmountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
