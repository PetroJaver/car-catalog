import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CarService} from "../../service/car.service";
import {Observable} from "rxjs";
import {Car} from "../../models/Car";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent{

  car:Car;

  id: number;
  constructor(private activateRoute: ActivatedRoute,private carService: CarService){

    this.id = activateRoute.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.carService.get(this.id).subscribe({next:(data: Car) => this.car=data});
  }

}
