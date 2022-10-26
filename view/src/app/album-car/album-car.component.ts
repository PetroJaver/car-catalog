import { Component, OnInit } from '@angular/core';
import {Car} from "../../models/Car";
import {CarService} from "../../service/car.service";
import {ToastrService} from "ngx-toastr";
import {Observable, Observer} from "rxjs";
@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit {

  cars: Car[];

  constructor(public carService: CarService,private toast: ToastrService) {
  }


  ngOnInit(): void {
    this.getCar()
  }

  delete(id:number): void {
    this.carService.delete(id).subscribe(response=>{
      this.getCar();
      this.toast.success("Car successful delete!","Success",{progressBar:true,timeOut:5000,progressAnimation: 'increasing'})
    })
  }

  getCar(): void{
    this.cars = [];
    this.carService.getAll().subscribe(data=>{
      this.cars = data;
      console.log(this.cars)
    })
  }

}

