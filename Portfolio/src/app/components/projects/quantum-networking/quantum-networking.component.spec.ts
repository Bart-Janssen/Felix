import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuantumNetworkingComponent } from './quantum-networking.component';

describe('QuantumNetworkingComponent', () => {
  let component: QuantumNetworkingComponent;
  let fixture: ComponentFixture<QuantumNetworkingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuantumNetworkingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuantumNetworkingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
