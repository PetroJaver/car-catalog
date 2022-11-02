import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Car} from "../models/Car";
import {Observable} from "rxjs";
@Injectable({
  providedIn: "root"
})
export class CarService{
  constructor(private http: HttpClient) {
  }

  getAll():Observable<Car[]>{
    return this.http.get<Car[]>('http://localhost:8080/cars');
  }

  get(id:number):Observable<Car>{
    return this.http.get<Car>('http://localhost:8080/cars/'+id);
  }

  delete(id: number):Observable<void> {
    return this.http.delete<void>('http://localhost:8080/cars/'+id);
  }

  add(data:FormData):Observable<void>{

    return this.http.post<void>('http://localhost:8080/cars',data);
  }

  update(data:FormData,id:number):Observable<void>{
    console.log(id)
    return this.http.put<void>('http://localhost:8080/cars/'+id,data);
  }

}
