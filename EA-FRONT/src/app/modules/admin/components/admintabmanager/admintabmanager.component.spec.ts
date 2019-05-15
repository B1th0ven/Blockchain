import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmintabmanagerComponent } from './admintabmanager.component';

describe('AdmintabmanagerComponent', () => {
  let component: AdmintabmanagerComponent;
  let fixture: ComponentFixture<AdmintabmanagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdmintabmanagerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmintabmanagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
