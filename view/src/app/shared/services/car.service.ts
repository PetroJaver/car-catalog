import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Car} from "../models/Car";
import {Observable} from "rxjs";
import {CarDto} from "../models/CarDto";

@Injectable({
  providedIn: "root"
})
export class CarService {
  baseUrl: string = "https://cars-peter-s-spring.implemica.com/cars/";
  constructor(private http: HttpClient) {
  }

  getAll(): Observable<Car[]> {
    return this.http.get<Car[]>(this.baseUrl);
  }

  get(id: number): Observable<Car> {
    return this.http.get<Car>(this.baseUrl + id);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + id);
  }

  add(car: CarDto): Observable<Car> {
    const headers = {'content-type': 'application/json'}
    console.log(JSON.stringify(car))
    return this.http.post<Car>(this.baseUrl, JSON.stringify(car),{'headers': headers});
  }

  update(car: CarDto, id: number): Observable<void> {
    const headers = {'content-type': 'application/json'}
    return this.http.put<void>(this.baseUrl + id, JSON.stringify(car), {'headers': headers});
  }

  uploadImage(body: FormData, id: number): Observable<void>{
    return this.http.post<void>(`${this.baseUrl}${id}/uploadImage`, body);
  }
}
