import {Component, OnInit} from '@angular/core';
import {Car} from "../shared/models/Car";
import {CarService} from "../shared/services/car.service";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/services/auth.service";
import {Title} from "@angular/platform-browser";
import {Brand} from "../shared/enums/Brand";

@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit{

  cars: Car[];

  noCars: boolean = false;

  constructor(public carService: CarService, private toast: ToastrService, public auth: AuthService,private titleService: Title) {
    this.titleService.setTitle('Car catalog');
  }

  ngOnInit(): void {
    this.getCar()

    // @ts-ignore
    $(document).ready(function() {
      // @ts-ignore
      $('body').tooltip({ selector: '[data-toggle=tooltip]', trigger : 'hover',delay: {"show": 800, "hide": 100}})
    });

  }

  delete(id: number): void {
    this.carService.delete(id).subscribe(response => {
      this.getCar();
      this.toast.success("Car successful delete!", "Delete success", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })
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

  getValueBrandByStringKey(key:string): any{
    for (let i = 0; i < Object.keys(Brand).length; i++) {
      if(Object.keys(Brand)[i] === key){
        return Object.values(Brand)[i].toString()
      }
    }
    return  "Brand not found";
  }
}

