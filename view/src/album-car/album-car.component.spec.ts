import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlbumCarComponent } from './album-car.component';

describe('AlbumCarComponent', () => {
  let component: AlbumCarComponent;
  let fixture: ComponentFixture<AlbumCarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlbumCarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlbumCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
