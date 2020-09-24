import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OsintComponent } from './osint.component';

describe('OsintComponent', () => {
  let component: OsintComponent;
  let fixture: ComponentFixture<OsintComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OsintComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OsintComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
