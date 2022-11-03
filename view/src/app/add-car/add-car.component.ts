import { Component, OnInit } from '@angular/core';
import {BodyType} from "./BodyType";
import {TransmissionType} from "./TransmissionType";
import {FormArray, FormBuilder, FormControl, FormGroup, Validator, Validators} from "@angular/forms";
import {CarService} from "../shared/service/car.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.css']
})
export class AddCarComponent implements OnInit {

  bodyTypes = BodyType;

  pathImage:string = "../../assets/add-image.png";

  transmissionTypes = TransmissionType;

  car = this.fb.group(
    {
      file:['',[Validators.required]],
      brand:['',[Validators.required,Validators.pattern('[^0-9]*'),Validators.minLength(2),Validators.maxLength(20)]],
      model:['',[Validators.required,Validators.minLength(2),Validators.maxLength(40)]],
      bodyType:['',[Validators.required]],
      year:[null,[Validators.max(2100),Validators.min(1880),Validators.required]],
      transmissionType:['',[Validators.required]],
      engineSize:[null,[Validators.max(10),Validators.min(0.1),Validators.required]],
      description:['',[Validators.required,Validators.minLength(50),Validators.maxLength(10000)]],
      shortDescription:['',[Validators.required,Validators.minLength(25),Validators.maxLength(150)]],
      optionalList: this.fb.array([
        this.fb.control('',[Validators.required,Validators.minLength(3),Validators.maxLength(30)])])


    }
  )

  get optionalList() {
    return this.car.get('optionalList') as FormArray;
  }

  get brand(){
    return this.car.get('brand')
  }

  get model(){
    return this.car.get('model')
  }

  get bodyType(){
    return this.car.get('bodyType')
  }

  get year(){
    return this.car.get('year')
  }

  get transmissionType(){
    return this.car.get('transmissionType')
  }

  get engineSize(){
    return this.car.get('engineSize')
  }

  get description(){
    return this.car.get('description')
  }

  get shortDescription(){
    return this.car.get('shortDescription')
  }

  get file(){
    return this.car.get('file')
  }


  addOption(){
    this.optionalList.push(this.fb.control('',[Validators.required,Validators.minLength(3),Validators.maxLength(30)]));
  }

  removeOption(i:number){
    this.optionalList.removeAt(i);
  }



  constructor(private fb:FormBuilder,private carService:CarService,private router:Router,private toast: ToastrService) {

  }

  onSubmit() {
    const data:FormData = new FormData();

    // @ts-ignore
    data.append('file',this.car.get('file')?.value)
    // @ts-ignore
    data.append('brand',this.car.get('brand')?.value)
    // @ts-ignore
    data.append('model',this.car.get('model')?.value)
    // @ts-ignore
    data.append('bodyType',this.car.get('bodyType')?.value)
    // @ts-ignore
    data.append('year',this.car.get('year')?.value)
    // @ts-ignore
    data.append('transmissionType',this.car.get('transmissionType')?.value)
    // @ts-ignore
    data.append('engineSize',this.car.get('engineSize')?.value)
    // @ts-ignore
    data.append('description',this.car.get('description')?.value)
    // @ts-ignore
    data.append('shortDescription',this.car.get('shortDescription')?.value)
    // @ts-ignore
    data.append('optionsList',this.car.get('optionalList')?.value)

    this.carService.add(data).subscribe(()=>{this.toast.success("Car successful add!","Success",{progressBar:true,timeOut:5000,progressAnimation: 'increasing'});
    this.router.navigate(['/'])})


  }

  ngOnInit(): void {
  }

  uploadFile(event:any){
    let filetype = event.target.files[0].type;
    if(filetype.match(/image\/png/)){
      let reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (event:any)=>{
        this.pathImage = event.target.result;
      }

      // @ts-ignore
      const image = (event.target as HTMLInputElement)?.files[0];

      this.car.patchValue({
        // @ts-ignore
        file: image
      });

      this.car.get('file')?.updateValueAndValidity();
    }else {
      this.car.patchValue({file:''})
      window.alert("Please select correct image format")
      this.pathImage = "../../assets/add-image.png";
    }
  }

}


