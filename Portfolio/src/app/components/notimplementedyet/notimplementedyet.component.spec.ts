import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotimplementedyetComponent } from './notimplementedyet.component';

describe('NotimplementedyetComponent', () => {
  let component: NotimplementedyetComponent;
  let fixture: ComponentFixture<NotimplementedyetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotimplementedyetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotimplementedyetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
