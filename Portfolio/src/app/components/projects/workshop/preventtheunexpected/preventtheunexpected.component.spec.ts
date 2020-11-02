import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreventtheunexpectedComponent } from './preventtheunexpected.component';

describe('PreventtheunexpectedComponent', () => {
  let component: PreventtheunexpectedComponent;
  let fixture: ComponentFixture<PreventtheunexpectedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreventtheunexpectedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreventtheunexpectedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
