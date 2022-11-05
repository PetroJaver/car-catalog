import {Injectable} from "@angular/core";
import {User} from "../interface/user";
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Router} from "@angular/router";

@Injectable(
  {
    providedIn: "root"
  }
)
export class AuthService {

  private token: any = null;

  constructor(private http: HttpClient, private router: Router) {
  }

  login(user: User): Observable<{ token: string }> {
    return this.http.post<{ token: string }>("http://localhost:8080/login", user).pipe(
      tap(
        ({token}) => {
          localStorage.setItem('auth-token', token)
          this.setToken(token)
          this.router.navigate(['/'])
        }));
  }

  setToken(token: string) {
    this.token = token;
  }

  getToken(): string {
    return this.token;
  }

  isAuthenticated(): boolean {
    return !!this.token
  }

  logout() {
    this.token = null;
    localStorage.clear();
    this.router.navigate(['/login'])
  }
}
