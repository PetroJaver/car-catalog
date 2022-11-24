import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AlbumCarComponent} from './album-car/album-car.component';
import {CarDetailsComponent} from './car-details/car-details.component';
import {RouterModule} from "@angular/router";
import {AddCarComponent} from './add-car/add-car.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {EditCarComponent} from './edit-car/edit-car.component';
import {LoginComponent} from './login/login.component';
import {TokenInterceptor} from "./shared/classes/token.interceptor";
import {AuthGuard} from "./shared/classes/auth.guard";

const routes = [
  {path: '', component: AlbumCarComponent},
  {path: 'details/:id', component: CarDetailsComponent},
  {path: 'add', component: AddCarComponent, canActivate: [AuthGuard]},
  {path: 'edit/:id', component: EditCarComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: '**',redirectTo:'', pathMatch: 'full'}

]

// @ts-ignore
@NgModule({
  declarations: [
    AppComponent,
    AlbumCarComponent,
    CarDetailsComponent,
    AddCarComponent,
    EditCarComponent,
    LoginComponent
  ],

  imports: [
    BrowserModule,
    HttpClientModule,
    // @ts-ignore
    RouterModule.forRoot(routes),
    FormsModule,
    ReactiveFormsModule,
    NgbModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({closeButton: true})
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    multi: true,
    useClass: TokenInterceptor
  }],
  bootstrap: [AppComponent]
})
export class AppModule {

}
