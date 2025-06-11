import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CheckInRequest} from "../model/CheckInRequest.model";
import {Observable} from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class AttendanceService {
  private apiUrl = 'http://localhost:8080/api/employee-attendance';
  constructor(private http: HttpClient) {}

  checkIn(request: CheckInRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/check-in`, request,{
      withCredentials: true
    });
  }

  checkOut(request: CheckInRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/check-out`, request,
      {
        withCredentials: true
      });
  }

}
