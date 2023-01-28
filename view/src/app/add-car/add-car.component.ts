import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {BodyType} from "../shared/enums/BodyType";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CarService} from "../shared/services/car.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {Brand} from "../shared/enums/Brand";
import {Title} from "@angular/platform-browser";
import {OptionType} from "../shared/enums/OptionType";
import {CarDto} from "../shared/models/CarDto";
import {Location} from "@angular/common";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-add-car',
  templateUrl: '../shared/templates/car-form.html',
    styleUrls: ['../shared/templates/car-form.css']
})
export class AddCarComponent implements OnInit,AfterViewInit {
  @ViewChild('optionalInput') optionalInput: ElementRef;
  @ViewChild('selectBrand') selectBrand:ElementRef;

  brandChoose = Brand;

  bodyTypes = BodyType;

  title:string = "Add car"

  pathImage: string;



  carForm:FormGroup = this.fb.group(
    {
      file: [null],
      brand: [null, Validators.required],
      model: [null, [Validators.required, Validators.pattern('["\'a-zA-Z0-9-\\s]*'), Validators.minLength(2), Validators.maxLength(40)]],
      bodyType: [null, [Validators.required]],
      year: [null, [Validators.max(2100), Validators.min(1880)]],
      transmissionType: [null, [Validators.required]],
      engineSize: [null, [Validators.max(10), Validators.min(0)]],
      description: [null, [Validators.minLength(50), Validators.maxLength(5000)]],
      shortDescription: [null, [Validators.minLength(25), Validators.maxLength(150)]],
      optional: [null, [Validators.pattern('[a-zA-Z0-9-\\s\']*'), Validators.minLength(2), Validators.maxLength(25)]],
      optionalList: this.fb.array([])
    }
  )

  constructor(private fb: FormBuilder, private carService: CarService, private router: Router,
              private toast: ToastrService, private titleService: Title, public location:Location,private modalService:NgbModal) {

    this.titleService.setTitle('Add car')
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

  ngAfterViewInit() {
    this.selectBrand.nativeElement.focus();
    window.scrollTo(0,0)
  }

  //region getters
  public colorSelectBrand():string{
    if(this.brand?.value==="")
      return "#6C757D"
    else
      return "#212529"
  }

  public colorSelectBody():string{
    if(this.bodyType?.value==="")
      return "#6C757D"
    else
      return "#212529"
  }


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

  get optionalList() {
    return this.carForm.get('optionalList') as FormArray
  }

  get file() {
    return this.carForm.get('file')
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
    this.carForm.reset()

    this.pathImage = "../../assets/add-image.png"

    this.addOptions()

    this.carForm.patchValue({
      transmissionType: 'MANUAL',
      brand: '',
      bodyType: ''
    })
  }

  onSubmit() {
    const car: CarDto = new CarDto;
    car.brand = this.carForm.get('brand')?.value;
    car.model = this.carForm.get('model')?.value;
    car.bodyType = this.carForm.get('bodyType')?.value;
    car.transmissionType = this.carForm.get('transmissionType')?.value;
    car.optionsList = this.carForm.get('optionalList')?.value;
    car.year = null;
    car.engineSize = null;


    if(this.description?.value!=""){
      car.description = this.description?.value;
    }

    if(this.shortDescription?.value!=""){
      car.shortDescription = this.shortDescription?.value;
    }

    if(this.engineSize?.value!=""&&!(this.engineSize?.value === null)){
      car.engineSize = parseFloat(Number(this.engineSize?.value).toFixed(1));
    }

    if(this.engineSize?.value === 0){
      car.engineSize = 0;
    }

    if(this.year?.value!=""&&this.year?.value!=null){
      car.year = Math.floor(Number(this.year?.value))
    }

    this.carService.add(car).subscribe((data) => {
      if(this.carForm.get('file')?.value!=null){
        const dataImage:FormData = new FormData();
        dataImage.append('image', this.carForm.get('file')?.value);

        this.carService.uploadImage(dataImage, data.id).subscribe(()=>{
          this.toast.success("Car successful added!", "Success", {
            progressBar: true,
            timeOut: 5000,
            progressAnimation: 'increasing'
          });
          this.location.back();
        },error => {
          if(error.status==404){
            this.toast.error("Car fail added!", "Fail", {
              progressBar: true,
              timeOut: 5000,
              progressAnimation: 'increasing'
            })
            this.router.navigate(['/'])
          }
        })
      }else{
        this.toast.success("Car successful added!", "Success", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        });
        this.router.navigate(['/'])
      }

    },error => {
      if(error.status==409){
        this.toast.info("Car already exist!", "Fail", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
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

      this.carForm.patchValue({
        // @ts-ignore
        file: image
      })

      this.carForm.get('file')?.updateValueAndValidity()
    } else {
      this.carForm.patchValue({file: null})
      this.toast.info("Please select correct image format!", "Invalid file", {
        progressBar: true,
        timeOut: 5000,
        progressAnimation: 'increasing'
      })
      this.pathImage = "../../assets/add-image.png"
    }
  }

  isYearInt():boolean{
    let num:number = this.year?.value;
    return (num % 1 === 0);
  }
}


