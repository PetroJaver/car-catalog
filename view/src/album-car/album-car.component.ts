import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {Car} from "../models/Car";
import {CarService} from "../service/car.service";

@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit {

  cars$:Observable<Car[]>;
  constructor(private carService: CarService) {
  }



  ngOnInit(): void {
    this.cars$ = this.carService.getAll()
  }

}
