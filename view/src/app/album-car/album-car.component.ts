import {Component, OnInit} from '@angular/core';
import {Car} from "../shared/models/Car";
import {CarService} from "../shared/service/car.service";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/service/auth.service";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit {

  cars: Car[];

  noCars: boolean = false;

  textDeleteButton: boolean[];

  textEditButton: boolean[];

  constructor(public carService: CarService, private toast: ToastrService, public auth: AuthService,private titleService: Title) {
    this.titleService.setTitle('car-catalog');
  }

  ngOnInit(): void {
    this.getCar()
    this.textDeleteButton = new Array(this.cars.length);
    this.textEditButton = new Array(this.cars.length);

    this.textDeleteButton.fill(false);
    this.textEditButton.fill(false);
  }

  delete(id: number): void {
    this.carService.delete(id).subscribe(response => {
      this.getCar();
      this.toast.success("Car successful delete!", "Delete success", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })
    },error => {
      if(error.status=403){
        this.toast.error("Maybe you are not logged in.", "Delete fail!", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
      }
    })
  }

  getCar(): void {
    this.cars = [];
    this.carService.getAll().subscribe(data => {
      this.cars = data;
      if (this.cars === null) {
        this.noCars = true;
      }
    })
  }
}

