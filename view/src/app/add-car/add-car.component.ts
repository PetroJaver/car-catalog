import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BodyType} from "./BodyType";
import {TransmissionType} from "./TransmissionType";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CarService} from "../shared/service/car.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Brand} from "./Brand";
import {Title} from "@angular/platform-browser";
import {OptionType} from "./OptionType";
import {CarDto} from "../shared/models/CarDto";
import {Location} from "@angular/common";

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.css']
})
export class AddCarComponent implements OnInit,AfterViewInit {
  @ViewChild('optionalInput') optionalInput: ElementRef;
  @ViewChild('selectBrand') selectBrand:ElementRef;

  brandChoose = Brand;

  bodyTypes = BodyType;

  transmissionTypes = TransmissionType;

  pathImage: string;



  car:FormGroup = this.fb.group(
    {
      file: [null],
      brand: [null, Validators.required],
      model: [null, [Validators.required, Validators.pattern('[a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: [null, [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: [null, [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0), Validators.required]],
      description: [null, [Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: [null, [Validators.minLength(25), Validators.maxLength(150)]],
      optional: [null, [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(3), Validators.maxLength(25)]],
      optionalList: this.fb.array([])
    }
  )

  constructor(private fb: FormBuilder, private carService: CarService, private router: Router,
              private toast: ToastrService, private titleService: Title, public location:Location) {

    this.titleService.setTitle('Add car in catalog')
    this.onReset()
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
  }

  ngAfterViewInit() {
    this.selectBrand.nativeElement.focus();
    window.scrollTo(0,0)
  }

  //region getters
  get optional() {
    return this.car.get('optional')
  }

  get brand() {
    return this.car.get('brand')
  }

  get model() {
    return this.car.get('model')
  }

  get bodyType() {
    return this.car.get('bodyType')
  }

  get year() {
    return this.car.get('year')
  }

  get transmissionType() {
    return this.car.get('transmissionType')
  }

  get engineSize() {
    return this.car.get('engineSize')
  }

  get description() {
    return this.car.get('description')
  }

  get shortDescription() {
    return this.car.get('shortDescription')
  }

  get optionalList() {
    return this.car.get('optionalList') as FormArray
  }

  get file() {
    return this.car.get('file')
  }
  //endregion


  addOption() {
    if(this.optional?.valid){
      this.optionalList.push(this.fb.control(this.optional?.value))
      this.optional?.reset('')
      this.optionalInput.nativeElement.focus()
    }
  }

  dropOption(index: number) {
    this.optionalList.removeAt(index)
  }

  private addOptions() {
    let arrayOptions = Object.values(OptionType)

    for (let i = this.optionalList.length - 1; i > -1; i--) {
      this.optionalList.removeAt(i)
    }

    for (let i = arrayOptions.length - 1; i > -1; i--)
      this.optionalList.push(this.fb.control(arrayOptions[i]))
  }

  onReset() {
    this.car.reset()

    this.pathImage = "../../assets/add-image.png"

    this.addOptions()

    this.car.patchValue({
      transmissionType: 'MANUAL',
      brand: '',
      bodyType: '',
      year: new Date().getFullYear(),
      engineSize: '1',
      description: null,
      shortDescription: null
    })
  }

  onSubmit() {
    const car: CarDto = new CarDto;
    car.brand = this.car.get('brand')?.value;
    car.model = this.car.get('model')?.value;
    car.bodyType = this.car.get('bodyType')?.value;
    car.year = Math.floor(Number(this.car.get('year')?.value))
    car.transmissionType = this.car.get('transmissionType')?.value;
    car.engineSize = parseFloat(Number(this.car.get('engineSize')?.value).toFixed(1))
    car.optionsList = this.car.get('optionalList')?.value;
    car.shortDescription = this.car.get('shortDescription')?.value;


    if(this.description?.value!=""){
      car.description = this.car.get('description')?.value;
    }

    this.carService.add(car).subscribe((data) => {


      if(this.car.get('file')?.value!=null){
        const dataImage:FormData = new FormData();
        dataImage.append('image', this.car.get('file')?.value);
        console.log(data.id)
        this.carService.uploadImage(dataImage, data.id).subscribe(()=>{
          this.toast.success("Car successful update!", "Success", {
            progressBar: true,
            timeOut: 5000,
            progressAnimation: 'increasing'
          });
          this.location.back();
        },error => {
          if(error.status==0||error.status==401||error.status==404){
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
        this.router.navigate(['/'])
      }

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
    let filetype = event.target.files[0].type
    if (filetype.match(/image\/png/)||filetype.match(/image\/jpeg/)) {
      let reader = new FileReader()
      reader.readAsDataURL(event.target.files[0])
      reader.onload = (event: any) => {
        this.pathImage = event.target.result
      }

      // @ts-ignore
      const image = (event.target as HTMLInputElement)?.files[0]

      this.car.patchValue({
        // @ts-ignore
        file: image
      })

      this.car.get('file')?.updateValueAndValidity()
    } else {
      this.car.patchValue({file: null})
      this.toast.info("Please select correct image format!", "Invalid file", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })
      this.pathImage = "../../assets/add-image.png"
    }
  }
}


