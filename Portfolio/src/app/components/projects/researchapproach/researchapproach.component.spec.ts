import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResearchapproachComponent } from './researchapproach.component';

describe('ResearchapproachComponent', () => {
  let component: ResearchapproachComponent;
  let fixture: ComponentFixture<ResearchapproachComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResearchapproachComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResearchapproachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
