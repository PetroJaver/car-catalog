import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../shared/service/car.service";
import {Car} from "../shared/models/Car";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/service/auth.service";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent {
  textDeleteButton: boolean = false;
  textEditButton: boolean = false;

  car: Car;

  textModalDeleteButton:boolean = false;
  textModalCloseButton:boolean = false;

  id: number;

  constructor(public auth: AuthService, private activateRoute: ActivatedRoute, private carService: CarService,
              private toast: ToastrService, private router: Router, private titleService: Title) {
    this.id = activateRoute.snapshot.params['id'];
  }

  delete(): void {
    this.carService.delete(this.id).subscribe(() => {
        this.toast.success("Car successful delete!", "Success", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })

      this.router.navigate(['/']);
      })
  }

  ngOnInit(): void {
    this.carService.get(this.id).subscribe((data) => {
      this.car = data;
      this.titleService.setTitle(`Details ${this.car.brand[0].toUpperCase()+this.car.brand.slice(1).toLowerCase()} ${this.car.model}`)}, error => {
      if(error.status=404){
        this.toast.error("Car id "+this.id+" does not exist!", "Not found!", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
        this.router.navigate(['/']);
      }
    })
  }

}
