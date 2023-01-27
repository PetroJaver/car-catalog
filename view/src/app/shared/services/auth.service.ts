import {Injectable} from "@angular/core";
import {User} from "../interfaces/user";
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

  login(user: User): Observable<{ token: string}> {
    return this.http.post<{ token: string}>("http://localhost:8080/login", user).pipe(
      tap(({token}) => {
          localStorage.setItem('auth-token', token)
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
