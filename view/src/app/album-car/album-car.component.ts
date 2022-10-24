import { Component, OnInit } from '@angular/core';
import {Car} from "../../models/Car";
import {CarService} from "../../service/car.service";
@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit {

  cars: Car[];

  constructor(public carService: CarService) {
  }


  ngOnInit(): void {
    this.getCar()
  }

  delete(id: number): void {
    this.carService.delete(id).subscribe(response=>{
      this.getCar();
    })
  }

  getCar(): void{
    this.cars = [];
    this.carService.getAll().subscribe(data=>{
      this.cars = data;
    })
  }

}

