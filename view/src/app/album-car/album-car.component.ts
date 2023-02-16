import {Component, OnInit} from '@angular/core';
import {Car} from "../shared/models/Car";
import {CarService} from "../shared/services/car.service";
import {ToastrService} from "ngx-toastr";
import {AuthService} from "../shared/services/auth.service";
import {Title} from "@angular/platform-browser";
import {Brand} from "../shared/enums/Brand";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
// @ts-ignore
import AOS from "aos";

@Component({
  selector: 'app-album-car',
  templateUrl: './album-car.component.html',
  styleUrls: ['./album-car.component.css']
})
export class AlbumCarComponent implements OnInit{

  cars: Car[];

  noCars: boolean = false;

  constructor(private modalService:NgbModal, public carService: CarService, private toast: ToastrService, public auth: AuthService,private titleService: Title) {
    this.titleService.setTitle('Car catalog');
  }

  ngOnInit(): void {
    AOS.init();
    this.getCar()
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
    },error => {
      if (error.status === 204){
        this.noCars = true
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

