import {Component, OnInit} from '@angular/core';
import {AuthService} from "./shared/services/auth.service";
import {Router} from "@angular/router";
import {Title} from "@angular/platform-browser";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  scrolled:boolean = false;

  firstName: string|null = localStorage.getItem('first-name');

  constructor(public auth: AuthService, private router: Router, private titleService: Title, private modalService:NgbModal) {
    this.titleService.setTitle('car-catalog');
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

  ngOnInit() {
    const potentialToken = localStorage.getItem('auth-token')
    if (potentialToken !== null) {
      this.auth.setToken(potentialToken)
    }

    window.addEventListener('storage', (event) => {
      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('auth-token');
        this.firstName = localStorage.getItem('first-name');
        if (token == undefined) {
          this.auth.logout();
          // @ts-ignore
          $('.modal').modal('hide');
        } else {
          this.auth.setToken(token);
        }
      }
    }, false);

    window.addEventListener('scroll', () => {
      if(window.scrollY!=0){
        this.scrolled=true;
      }else {
        this.scrolled=false;
      }
    }, false);
  }

  scrollToTop(){
    window.scrollTo(0,0)
  }
}
