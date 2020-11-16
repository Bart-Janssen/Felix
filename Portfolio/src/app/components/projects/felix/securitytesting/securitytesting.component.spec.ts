import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecuritytestingComponent } from './securitytesting.component';

describe('SecuritytestingComponent', () => {
  let component: SecuritytestingComponent;
  let fixture: ComponentFixture<SecuritytestingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecuritytestingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecuritytestingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
