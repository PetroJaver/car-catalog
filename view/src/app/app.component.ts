import {Component, OnInit} from '@angular/core';
import {AuthService} from "./shared/service/auth.service";
import {Router} from "@angular/router";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  textLogInButton:boolean = false;
  textLogOutButton:boolean = false;
  textAddCarButton:boolean = false;

  constructor(public auth: AuthService, private router: Router,private titleService: Title) {
    this.titleService.setTitle('car-catalog');
  }

  ngOnInit() {
    const potentialToken = localStorage.getItem('auth-token')
    if (potentialToken !== null) {
      this.auth.setToken(potentialToken)
    }

    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('auth-token');
        if (token == undefined) {
          this.auth.logout();
        } else {
          this.auth.setToken(token)
        }
      }
    }, false);
  }
}
