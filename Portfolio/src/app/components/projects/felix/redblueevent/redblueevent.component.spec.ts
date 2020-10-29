import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RedblueeventComponent } from './redblueevent.component';

describe('RedblueeventComponent', () => {
  let component: RedblueeventComponent;
  let fixture: ComponentFixture<RedblueeventComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RedblueeventComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RedblueeventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
