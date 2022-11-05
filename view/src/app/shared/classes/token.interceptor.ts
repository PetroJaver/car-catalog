import {Injectable} from "@angular/core";
import {AuthService} from "../service/auth.service";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Router} from "@angular/router";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private auth: AuthService, private router: Router) {

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
      console.log(error.status)
      if (error.status == 401 || error.status == 0) {
        this.auth.logout()
        this.router.navigate(['/login'])
      }

      return throwError(error)
    }))
  }
}
