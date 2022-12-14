import {Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService, private router: Router,private toast:ToastrService) {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.auth.isAuthenticated()) {
      req = req.clone({
        setHeaders: {
          Authorization: this.auth.getToken()
        }
      })
    }

    return next.handle(req).pipe(catchError(error => {
      if (error.status === 401 || error.status === 0) {
        this.auth.logout()
        this.router.navigate(['/login'])
        this.toast.error("Authorization time out!", "Unauthorized!", {
          progressBar: true,
          timeOut: 5000,
          progressAnimation: 'increasing'
        })
      }

      return throwError(error)
    }))
  }
}
