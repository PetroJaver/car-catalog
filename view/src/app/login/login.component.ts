import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../shared/service/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  user: FormGroup = this.fb.group({
    email: ['', [Validators.email, Validators.required]],
    password: ['', [Validators.minLength(4), Validators.required]]
  })

  constructor(private fb: FormBuilder, private auth: AuthService, private toast: ToastrService) {

  }

  get email() {
    return this.user.get('username');
  }

  get password() {
    return this.user.get('password');
  }

  ngOnInit(): void {
  }

  onSubmit() {
    this.auth.login(this.user.value).subscribe(
      () => {
        this.toast.success("You successfully logged in!", "Success", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        });
      }, error => {
        if (error.status == 403) {
          this.toast.error("Invalid password or username!", "Wrong", {
            progressBar: true,
            timeOut: 2000,
            progressAnimation: 'increasing'
          })
        }
      })

  }
}
