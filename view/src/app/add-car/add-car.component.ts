import {AfterViewInit, Component, ElementRef, OnInit, QueryList, ViewChild} from '@angular/core';
import {BodyType} from "./BodyType";
import {TransmissionType} from "./TransmissionType";
import {FormArray, FormBuilder, Validators} from "@angular/forms";
import {CarService} from "../shared/service/car.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Brand} from "./Brand";
import {Title} from "@angular/platform-browser";
import {OptionType} from "./OptionType";
import {AuthService} from "../shared/service/auth.service";
import {CarDto} from "../shared/models/CarDto";

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

  textAddCarButton: boolean = false;

  textResetCarButton: boolean =false;



  car = this.fb.group(
    {
      file: [null],
      brand: ['', Validators.required],
      model: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: ['', [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: ['', [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0), Validators.required]],
      description: ["", [Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: ["", [Validators.minLength(25), Validators.maxLength(150)]],
      optional: ['', [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(3), Validators.maxLength(25)]],
      optionalList: this.fb.array([])
    }
  )

  constructor(private fb: FormBuilder, private carService: CarService, private router: Router,
              private toast: ToastrService, private titleService: Title,private auth:AuthService) {

    this.titleService.setTitle('add-car')
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

  get engineValue(){
    return Number(this.engineSize?.value)
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
      //@ts-ignore
      year: new Date().getFullYear(),
      //@ts-ignore
      engineSize: '1',
      description: "",
      shortDescription: ""
    })
  }

  onSubmit() {
    const data: FormData = new FormData()
    let carDto:CarDto = new CarDto();

    carDto.optionsList =  this.optionalList.value;
    // @ts-ignore
    carDto.brand = this.car.get('brand')?.value;
    // @ts-ignore
    carDto.model = this.car.get('model')?.value;
    // @ts-ignore
    carDto.bodyType = this.car.get('bodyType')?.value;
    // @ts-ignore
    carDto.year = this.car.get('year')?.value;
    // @ts-ignore
    carDto.transmissionType = this.car.get('transmissionType')?.value;
    // @ts-ignore
    carDto.engineSize = this.car.get('engineSize')?.value;
    // @ts-ignore
    if(this.description?.value!=""){
      // @ts-ignore
      carDto.description = this.car.get('description')?.value;
    }
    // @ts-ignore
    if(this.shortDescription?.value!=""){
      // @ts-ignore
      carDto.shortDescription = this.car.get('shortDescription')?.value;
    }


    if(this.car.get('file')?.value!=null){
      // @ts-ignore
      data.append('file', this.car.get('file')?.value)
    }


    data.append('carDto', new Blob([JSON.stringify(carDto)], { type: 'application/json' }));

    this.carService.add(data).subscribe(() => {
      this.toast.success("Car successful add!", "Success", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })
      this.router.navigate(['/'])
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
    if (filetype.match(/image\/png/)) {
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


