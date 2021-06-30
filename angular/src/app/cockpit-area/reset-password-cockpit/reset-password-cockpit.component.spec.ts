import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import { ResetPasswordCockpitComponent } from './reset-password-cockpit.component';

describe('ResetPasswordCockpitComponent', () => {
  let component: ResetPasswordCockpitComponent;
  let fixture: ComponentFixture<ResetPasswordCockpitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ResetPasswordCockpitComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetPasswordCockpitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
