import {Injectable} from "@angular/core";
import {User} from "../interface/user";
import {HttpClient} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Location} from "@angular/common";

@Injectable(
  {
    providedIn: "root"
  }
)
export class AuthService {
  private token: any = null;

  constructor(private http: HttpClient,private location:Location) {
  }

  login(user: User): Observable<{ token: string,firstName:string }> {
    return this.http.post<{ token: string,firstName:string }>("http://localhost:8080/login", user).pipe(
      tap(({token, firstName}) => {
          localStorage.setItem('auth-token', token)
          localStorage.setItem('first-name', firstName)
          this.setToken(token);
          this.location.back();
        }));
  }

  setToken(token: any) {
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
  }
}
