import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersoonlijkeOntwikkelingComponent } from './persoonlijke-ontwikkeling.component';

describe('PersoonlijkeOntwikkelingComponent', () => {
  let component: PersoonlijkeOntwikkelingComponent;
  let fixture: ComponentFixture<PersoonlijkeOntwikkelingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersoonlijkeOntwikkelingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersoonlijkeOntwikkelingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
