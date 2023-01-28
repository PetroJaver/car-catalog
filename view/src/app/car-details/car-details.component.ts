import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CarService} from "../shared/services/car.service";
import {Car} from "../shared/models/Car";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/services/auth.service";
import {Title} from "@angular/platform-browser";
import {Brand} from "../shared/enums/Brand";
import {BodyType} from "../shared/enums/BodyType";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details.component.html',
  styleUrls: ['./car-details.component.css']
})
export class CarDetailsComponent {

  car: Car;

  id: number;

  constructor(public auth: AuthService, private activateRoute: ActivatedRoute, private carService: CarService,
              private toast: ToastrService, private router: Router, private titleService: Title, private modalService:NgbModal) {
    this.id = activateRoute.snapshot.params['id'];
  }

  // @ts-ignore
  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title', centered: true, size: "sm"}).result.then((result) => {
      // @ts-ignore
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      // @ts-ignore
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    // @ts-ignore
    if (reason === ModalOfDismissReasons.ESC) {
      return 'by pressing ESC';
    } else { // @ts-ignore
      if (reason === ModalOfDismissReasons.BACKDROP_CLICK) {
        return 'by clicking on a backdrop';
      } else {
        return  `with: ${reason}`;
      }
    }
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

  getValueBodyTypeByStringKey(key:string): any{
    for (let i = 0; i < Object.keys(BodyType).length; i++) {
      if(Object.keys(BodyType)[i] === key){
        return Object.values(BodyType)[i].toString()
      }
    }
    return  "Brand not found";
  }
}
