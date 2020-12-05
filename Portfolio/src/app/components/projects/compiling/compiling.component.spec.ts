import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompilingComponent } from './compiling.component';

describe('CompilingComponent', () => {
  let component: CompilingComponent;
  let fixture: ComponentFixture<CompilingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompilingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompilingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
