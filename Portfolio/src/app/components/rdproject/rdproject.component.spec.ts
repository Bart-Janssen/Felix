import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RdprojectComponent } from './rdproject.component';

describe('RdprojectComponent', () => {
  let component: RdprojectComponent;
  let fixture: ComponentFixture<RdprojectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RdprojectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RdprojectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
