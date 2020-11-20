import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OnderzoeksplanComponent } from './onderzoeksplan.component';

describe('OnderzoeksplanComponent', () => {
  let component: OnderzoeksplanComponent;
  let fixture: ComponentFixture<OnderzoeksplanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OnderzoeksplanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OnderzoeksplanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
