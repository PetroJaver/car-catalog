import {Component, ElementRef, OnInit, QueryList, ViewChild} from '@angular/core';
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

@Component({
  selector: 'app-add-car',
  templateUrl: './add-car.component.html',
  styleUrls: ['./add-car.component.css']
})
export class AddCarComponent implements OnInit {
  @ViewChild('optionalInput') optionalInput: ElementRef;

  brandChoose = Brand;

  bodyTypes = BodyType;

  transmissionTypes = TransmissionType;

  baseOptions: string[] = Object.values(OptionType);

  addedOptions: string[] = [];

  pathImage: string = "../../assets/add-image.png";

  textAddCarButton: boolean = false;

  textResetCarButton: boolean =false;

  car = this.fb.group(
    {
      file: ['', Validators.required],
      brand: ['', Validators.required],
      model: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: ['', [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880), Validators.required]],
      transmissionType: ['MANUAL', [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0.1), Validators.required]],
      description: [null, [Validators.required, Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: ['', [Validators.required, Validators.minLength(25), Validators.maxLength(150)]],
      optional: ['', [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(3), Validators.maxLength(25)]],
      optionalCheckBox: this.fb.array([])
    }
  )

  constructor(private fb: FormBuilder, private carService: CarService, private router: Router,
              private toast: ToastrService, private titleService: Title,private auth:AuthService) {

    this.titleService.setTitle('add-car')
    this.addCheckboxes()
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

  get optionalCheckBox() {
    return this.car.get('optionalCheckBox') as FormArray
  }

  get file() {
    return this.car.get('file')
  }

  //endregion


  addOption() {
    // @ts-ignore
    this.addedOptions.push(this.optional?.value)
    this.optionalCheckBox.push(this.fb.control(true))
    this.optional?.reset('')
    this.optionalInput.nativeElement.focus()
  }

  dropOption(index: number) {
    this.addedOptions.splice(index, 1)
    this.optionalCheckBox.removeAt(index+(this.baseOptions.length-1))
  }



  private addCheckboxes() {
    for (let i = this.optionalCheckBox.length - 1; i > -1; i--) {
      this.optionalCheckBox.removeAt(i)
    }

    this.baseOptions.forEach(() => this.optionalCheckBox.push(this.fb.control(false)))
  }

  onReset() {
    this.car.reset()

    this.addCheckboxes()
    this.addedOptions = [];

    this.car.patchValue({
      transmissionType: 'MANUAL',
      brand: '',
      bodyType: ''
    })
  }

  onSubmit() {
    const data: FormData = new FormData()
    let optionListData: string[] = []
    let lengthBaseOptions = this.baseOptions.length


    for (let i = 0; i < this.optionalCheckBox.length; i++) {
      if (i < lengthBaseOptions && this.optionalCheckBox.at(i).value)
        optionListData.push(this.baseOptions[i])

      if (i >= lengthBaseOptions && this.optionalCheckBox.at(i).value)
        optionListData.push(this.addedOptions[i - lengthBaseOptions])
    }

    // @ts-ignore
    data.append('file', this.car.get('file')?.value)
    // @ts-ignore
    data.append('brand', this.car.get('brand')?.value)
    // @ts-ignore
    data.append('model', this.car.get('model')?.value)
    // @ts-ignore
    data.append('bodyType', this.car.get('bodyType')?.value)
    // @ts-ignore
    data.append('year', this.car.get('year')?.value)
    // @ts-ignore
    data.append('transmissionType', this.car.get('transmissionType')?.value)
    // @ts-ignore
    data.append('engineSize', this.car.get('engineSize')?.value)
    // @ts-ignore
    data.append('description', this.car.get('description')?.value)
    // @ts-ignore
    data.append('shortDescription', this.car.get('shortDescription')?.value)
    // @ts-ignore
    data.append('optionsList', optionListData)

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
      this.car.patchValue({file: ''})
      window.alert("Please select correct image format")
      this.pathImage = "../../assets/add-image.png"
    }
  }
}


