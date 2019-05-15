import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsManagerComponent } from './tabs-manager.component';

describe('TabsManagerComponent', () => {
  let component: TabsManagerComponent;
  let fixture: ComponentFixture<TabsManagerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TabsManagerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabsManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
