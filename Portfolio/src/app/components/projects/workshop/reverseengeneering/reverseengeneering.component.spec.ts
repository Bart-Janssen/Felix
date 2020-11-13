import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReverseengeneeringComponent } from './reverseengeneering.component';

describe('ReverseengeneeringComponent', () => {
  let component: ReverseengeneeringComponent;
  let fixture: ComponentFixture<ReverseengeneeringComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReverseengeneeringComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReverseengeneeringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
