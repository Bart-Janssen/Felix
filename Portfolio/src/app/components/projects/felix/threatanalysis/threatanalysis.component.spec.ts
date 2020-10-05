import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ThreatanalysisComponent } from './threatanalysis.component';

describe('ThreatanalysisComponent', () => {
  let component: ThreatanalysisComponent;
  let fixture: ComponentFixture<ThreatanalysisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ThreatanalysisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ThreatanalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
