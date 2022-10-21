import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Car} from "../models/Car";

@Injectable({
  providedIn: "root"
})
export class CarService{
  constructor(private http: HttpClient) {
  }

  getAll():Observable<Car[]>{
    return this.http.get<Car[]>('http://localhost:8080/cars');
  }

}
