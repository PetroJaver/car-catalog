import {Component, OnInit} from '@angular/core';
import {BodyType} from "../add-car/BodyType";
import {TransmissionType} from "../add-car/TransmissionType";
import {FormArray, FormBuilder, FormControl, FormGroup, Validator, Validators} from "@angular/forms";
import {CarService} from "../shared/service/car.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Car} from "../shared/models/Car";

@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.css']
})
export class EditCarComponent implements OnInit {

  car: Car;

  id: number;

  pathPart: string = 'http://localhost:8080/cars/images/';

  pathImage: string;

  bodyTypes = BodyType;

  transmissionTypes = TransmissionType;

  carForm: FormGroup = this.fb.group(
    {
      file: [''],
      brand: ['', [Validators.required, Validators.pattern('[^0-9]*'), Validators.minLength(2), Validators.maxLength(20)]],
      model: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: ['', [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: ['', [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0.1), Validators.required]],
      description: ['', [Validators.required, Validators.minLength(50), Validators.maxLength(10000)]],
      shortDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(150)]],
      optionalList: this.fb.array([
        this.fb.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)])])


    }
  );


  get optionalList() {
    return this.carForm.get('optionalList') as FormArray;
  }

  //region getter
  get brand() {
    return this.carForm.get('brand')
  }

  get model() {
    return this.carForm.get('model')
  }

  get bodyType() {
    return this.carForm.get('bodyType')
  }

  get year() {
    return this.carForm.get('year')
  }

  get transmissionType() {
    return this.carForm.get('transmissionType')
  }

  get engineSize() {
    return this.carForm.get('engineSize')
  }

  get description() {
    return this.carForm.get('description')
  }

  get shortDescription() {
    return this.carForm.get('shortDescription')
  }

  get file() {
    return this.carForm.get('file')
  }

  //endregion


  addOption() {
    this.optionalList.push(this.fb.control('', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]));
  }

  removeOption(i: number) {
    this.optionalList.removeAt(i);
  }


  constructor(private activateRoute: ActivatedRoute, private fb: FormBuilder, private carService: CarService, private router: Router, private toast: ToastrService) {
    this.id = activateRoute.snapshot.params['id'];

  }

  onReset() {
    this.pathImage = this.pathPart + this.car.id+'/'+this.car.imageName;

    let optionalArr: string[] = this.car.optionsList;

    this.optionalList.clear();

    for (let option of optionalArr) {
      this.optionalList.push(this.fb.control(option, [Validators.required, Validators.minLength(3), Validators.maxLength(30)]));
    }

    this.carForm.patchValue({
      brand: this.car.brand,
      model: this.car.model,
      bodyType: this.car.bodyType,
      transmissionType: this.car.transmissionType,
      engineSize: this.car.engineSize,
      year: this.car.year,
      shortDescription: this.car.shortDescription,
      description: this.car.description
    })

    fetch(this.pathImage)
      .then(res => res.blob())
      .then(blob => {
        let image: File = new File([blob], this.car.imageName)
        this.carForm.patchValue({file: image})
      })
  }

  onSubmit() {
    const data: FormData = new FormData();

    data.append('file', this.carForm.get('file')?.value)
    // @ts-ignore
    data.append('brand', this.carForm.get('brand')?.value)
    // @ts-ignore
    data.append('model', this.carForm.get('model')?.value)
    // @ts-ignore
    data.append('bodyType', this.carForm.get('bodyType')?.value)
    // @ts-ignore
    data.append('year', this.carForm.get('year')?.value)
    // @ts-ignore
    data.append('transmissionType', this.carForm.get('transmissionType')?.value)
    // @ts-ignore
    data.append('engineSize', this.carForm.get('engineSize')?.value)
    // @ts-ignore
    data.append('description', this.carForm.get('description')?.value)
    // @ts-ignore
    data.append('shortDescription', this.carForm.get('shortDescription')?.value)
    // @ts-ignore
    data.append('optionsList',this.carForm.get('optionalList')?.value)

    this.carService.update(data, this.id).subscribe((data) => {
        this.toast.success("Car successful update!", "Success", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        });
        this.router.navigate(['/']);
      })


  }

  ngOnInit(): void {
    this.carService.get(this.id).subscribe((data: Car) => {
      this.car = data;

      this.onReset();
    });
  }

  uploadFile(event: any) {
    let filetype = event.target.files[0].type;
    if (filetype.match(/image\/png/)) {
      let reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (event: any) => {
        this.pathImage = event.target.result;
      }

      // @ts-ignore
      const image = (event.target as HTMLInputElement)?.files[0];

      this.carForm.patchValue({
        // @ts-ignore
        file: image
      });

      this.carForm.get('file')?.updateValueAndValidity();
    } else {
      this.carForm.patchValue({file: ''})
      window.alert("Please select correct image format")
      this.pathImage = this.pathPart + this.car.id+'/'+this.car.imageName;
    }
  }

  /*"../../assets/add-image.png"*/
}
