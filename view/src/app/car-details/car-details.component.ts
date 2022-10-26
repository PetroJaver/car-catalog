import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../../service/car.service";
import {Observable} from "rxjs";
import {Car} from "../../models/Car";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent {

  car: Car;

  id: number;

  constructor(private activateRoute: ActivatedRoute, private carService: CarService, private toast: ToastrService, private router: Router) {

    this.id = activateRoute.snapshot.params['id'];
  }

  delete(id: number): void {
    this.carService.delete(id).subscribe(() => this.toast.success("Car successful delete!", "Success", {
      progressBar: true,
      timeOut: 5000,
      progressAnimation: 'increasing'
    }))
    this.router.navigate(['/']);
  }

  ngOnInit(): void {
    this.carService.get(this.id).subscribe((data) =>{ this.car = data;
    console.log(this.car)})
  }

}
