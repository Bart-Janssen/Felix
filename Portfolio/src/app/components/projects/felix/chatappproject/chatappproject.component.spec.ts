import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatappprojectComponent } from './chatappproject.component';

describe('ChatappprojectComponent', () => {
  let component: ChatappprojectComponent;
  let fixture: ComponentFixture<ChatappprojectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChatappprojectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChatappprojectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
