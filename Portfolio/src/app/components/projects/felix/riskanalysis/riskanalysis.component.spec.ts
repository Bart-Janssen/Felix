import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RiskanalysisComponent } from './riskanalysis.component';

describe('RiskanalysisComponent', () => {
  let component: RiskanalysisComponent;
  let fixture: ComponentFixture<RiskanalysisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RiskanalysisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RiskanalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
