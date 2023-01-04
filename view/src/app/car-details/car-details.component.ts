import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../shared/service/car.service";
import {Car} from "../shared/models/Car";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/service/auth.service";
import {Title} from "@angular/platform-browser";
import {Brand} from "../add-car/Brand";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent {

  car: Car;

  id: number;

  constructor(public auth: AuthService, private activateRoute: ActivatedRoute, private carService: CarService,
              private toast: ToastrService, private router: Router, private titleService: Title) {
    this.id = activateRoute.snapshot.params['id'];
  }

  delete(): void {
    this.carService.delete(this.id).subscribe(() => {
        this.toast.success("Car successful delete!", "Delete success", {
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
      console.log(data)
      this.titleService.setTitle(`Details ${this.getValueBrandByStringKey(this.car.brand)} ${this.car.model}`)}, error => {
      if(error.status===404){
        this.toast.error("Car id "+this.id+" does not exist!", "Not found!", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
        this.router.navigate(['/']);
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
