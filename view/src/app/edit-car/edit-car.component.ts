import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {BodyType} from "../add-car/BodyType";
import {TransmissionType} from "../add-car/TransmissionType";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CarService} from "../shared/service/car.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Car} from "../shared/models/Car";
import {AuthService} from "../shared/service/auth.service";
import {Title} from "@angular/platform-browser";
import {Brand} from "../add-car/Brand";
import { Location } from '@angular/common'
@Component({
  selector: 'app-edit-car',
  templateUrl: './edit-car.component.html',
  styleUrls: ['./edit-car.component.css']
})
export class EditCarComponent implements OnInit {
  @ViewChildren('listInputOptional') listInputOptional: QueryList<ElementRef>;


  @ViewChild('optionalInput') optionalInput: ElementRef;

  car: Car;

  id: number;

  pathPart: string = 'https://carcatalogimages.s3.eu-west-3.amazonaws.com/';

  pathImage: string;

  isUploadImage = false;

  bodyTypes = BodyType;

  brandChoose = Brand;

  transmissionTypes = TransmissionType;

  textAddCarButton: boolean = false;

  textResetCarButton: boolean = false;

  carForm: FormGroup = this.fb.group(
    {
      file: [''],
      brand: ['', Validators.required],
      model: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: ['', [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: ['MANUAL', [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0.1), Validators.required]],
      description: [null, [Validators.required, Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(150)]],
      optional: ['', [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(3), Validators.maxLength(25)]],
      optionalList: this.fb.array([])
    }
  );

  constructor(private auth: AuthService,private location: Location, private activateRoute: ActivatedRoute, private fb: FormBuilder, private carService: CarService, private router: Router, private toast: ToastrService, private titleService: Title) {
    this.id = activateRoute.snapshot.params['id'];
    this.titleService.setTitle('edit-car/' + this.id);
  }

  ngOnInit(): void {
    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('auth-token');
        if (token == undefined) {
          this.router.navigate(['/'])
        }
      }
    }, false);

    this.carService.get(this.id).subscribe((data: Car) => {
      this.car = data;

      this.onReset();
    }, error => {
      if(error.status=404){
        this.toast.error("Car number "+this.id+" does not exist!", "Not found!", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
        this.router.navigate(['/']);
      }
    })
  }

  //region getter
  get optional() {
    return this.carForm.get('optional')
  }

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

  get optionalList() {
    return this.carForm.get('optionalList') as FormArray;
  }

  //endregion


  addOption() {
    this.optionalList.push(this.fb.control(this.optional?.value))
    this.optional?.reset('')
    this.optionalInput.nativeElement.focus()
  }

  dropOption(index: number) {
    this.optionalList.removeAt(index)
  }

  private addOptions() {
    for (let i = this.optionalList.length - 1; i > -1; i--) {
      this.optionalList.removeAt(i)
    }

    for (let i = this.car.optionsList.length - 1; i > -1; i--)
      this.optionalList.push(this.fb.control(this.car.optionsList[i]))
  }

  onReset() {
    this.pathImage = this.pathPart + this.car.imageName;

    this.isUploadImage = false;

    this.carForm.reset()

    this.addOptions()

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
  }

  onSubmit() {
    const data: FormData = new FormData();

    if(this.isUploadImage){
      data.append('file', this.carForm.get('file')?.value)
    }

    data.append('brand', this.carForm.get('brand')?.value)
    data.append('model', this.carForm.get('model')?.value)
    data.append('bodyType', this.carForm.get('bodyType')?.value)
    data.append('year', this.carForm.get('year')?.value)
    data.append('transmissionType', this.carForm.get('transmissionType')?.value)
    data.append('engineSize', this.carForm.get('engineSize')?.value)
    data.append('description', this.carForm.get('description')?.value)
    data.append('shortDescription', this.carForm.get('shortDescription')?.value)
    data.append('optionsList', this.carForm.get('optionalList')?.value)

    this.carService.update(data, this.id).subscribe((data) => {
      this.toast.success("Car successful update!", "Success", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      });
        this.location.back();
    }, error => {
      if(error.status==0||error.status==401){
        this.toast.error("Car fail add!", "Fail", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
        this.router.navigate(['/'])
      }
    })


  }


  uploadFile(event: any) {
    let filetype = event.target.files[0].type;
    if (filetype.match(/image\/png/)) {
      let reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (event: any) => {
        this.pathImage = event.target.result;
        this.isUploadImage = true;
      }

      // @ts-ignore
      const image = (event.target as HTMLInputElement)?.files[0];

      this.carForm.patchValue({
        file: image
      });

      this.carForm.get('file')?.updateValueAndValidity();
    } else {
      this.carForm.patchValue({file: ''})

      window.alert("Please select correct image format")
      this.pathImage = this.pathPart + this.car.imageName;

      this.isUploadImage = false;
    }
  }
}
