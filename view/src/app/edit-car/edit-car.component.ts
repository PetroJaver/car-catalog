import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
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
import {Location} from '@angular/common'
import {CarDto} from "../shared/models/CarDto";

@Component({
  selector: 'app-edit-car',
  templateUrl: '../shared/templates/car-form.html',
  styleUrls: ['../shared/templates/car-form.css']
})
export class EditCarComponent implements OnInit,AfterViewInit {
  @ViewChildren('listInputOptional') listInputOptional: QueryList<ElementRef>;

  @ViewChild('selectBrand') selectBrand:ElementRef;
  @ViewChild('optionalInput') optionalInput: ElementRef;

  car: Car;

  title:string;

  id: number;

  pathPart: string = 'https://carcatalogimages.s3.eu-west-3.amazonaws.com/';

  pathImage: string;

  isUploadImage = false;

  bodyTypes = BodyType;

  brandChoose = Brand;

  transmissionTypes = TransmissionType;

  carForm: FormGroup = this.fb.group(
    {
      file: [''],
      brand: ['', Validators.required],
      model: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: ['', [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: ['MANUAL', [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0), Validators.required]],
      description: [null, [Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: ['', [Validators.minLength(25), Validators.maxLength(150)]],
      optional: ['', [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(3), Validators.maxLength(25)]],
      optionalList: this.fb.array([])
    }
  );

  constructor(private auth: AuthService,public location: Location, private activateRoute: ActivatedRoute, private fb: FormBuilder, private carService: CarService, private router: Router, private toast: ToastrService, private titleService: Title) {
    this.id = activateRoute.snapshot.params['id'];
  }

  ngOnInit(): void {
    // @ts-ignore
    $(document).ready(function() {
      // @ts-ignore
      $('.tooltip').not(this).hide();
    });

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
      let brand:string;
      // @ts-ignore
      this.title = `Edit ${this.car.brand[0].toUpperCase()+this.car.brand.slice(1).toLowerCase()} ${this.car.model}`
      this.titleService.setTitle(`Edit ${this.car.brand[0].toUpperCase()+this.car.brand.slice(1).toLowerCase()} ${this.car.model}`);
      this.onReset();
    }, error => {
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

  ngAfterViewInit() {
    this.selectBrand.nativeElement.focus();
    window.scrollTo(0,0);
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
    const car: CarDto = new CarDto;
    car.brand = this.carForm.get('brand')?.value;
    car.model = this.carForm.get('model')?.value;
    car.bodyType = this.carForm.get('bodyType')?.value;
    car.year = Math.floor(Number(this.carForm.get('year')?.value))
    car.transmissionType = this.carForm.get('transmissionType')?.value;
    car.engineSize = parseFloat(Number(this.carForm.get('engineSize')?.value).toFixed(1))
    car.optionsList = this.carForm.get('optionalList')?.value;
    if(this.carForm.get('description')?.value===""){
      // @ts-ignore
      car.description = null;
    }else {
      car.description = this.carForm.get('description')?.value;
    }

    if(this.carForm.get('shortDescription')?.value===""){
      // @ts-ignore
      car.shortDescription = null;
    }else {
      car.shortDescription = this.carForm.get('shortDescription')?.value;
    }

    this.carService.update(car, this.id).subscribe(() => {
      if(this.carForm.get('file')?.value!=null){
        const dataImage:FormData = new FormData();
        dataImage.append('image', this.carForm.get('file')?.value);
        this.carService.uploadImage(dataImage, this.id).subscribe(()=>{
          this.toast.success("Car successful update!", "Success", {
            progressBar: true,
            timeOut: 5000,
            progressAnimation: 'increasing'
          });
          this.location.back();
        },error => {
          if(error.status==404){
            this.toast.error("Car fail add!", "Fail", {
              progressBar: true,
              timeOut: 5000,
              progressAnimation: 'increasing'
            })
            this.router.navigate(['/'])
          }
        })
      }else{
          this.toast.success("Car successful update!", "Success", {
            progressBar: true,
            timeOut: 5000,
            progressAnimation: 'increasing'
          });
          this.location.back();
      }
    }, error => {
      if(error.status===400||error.status===404){
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
      this.carForm.patchValue({file: null})
      this.toast.info("Please select correct image format!", "Invalid file", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })

      this.pathImage = this.pathPart + this.car.imageName;

      this.isUploadImage = false;
    }
  }
}
