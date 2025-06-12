import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {CheckInRequest} from "../model/CheckInRequest.model";
import {Observable} from "rxjs";
@Injectable({
  providedIn: 'root'
})
export class AttendanceService {

  private apiUrl = 'http://localhost:8080/api/employee-attendance';
  constructor(private http: HttpClient) {}

  checkIn(request: CheckInRequest): Observable<any> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`  // 🔁 실제 토큰이면 여기에 삽입
    });

    return this.http.post(`${this.apiUrl}/check-in`, request, {
      headers: headers,
      withCredentials: true
    });
  }

  checkOut(request: CheckInRequest): Observable<any> {
    const token = localStorage.getItem('token') || '';
    return this.http.post(`${this.apiUrl}/check-out`, request,
      {
        headers: {
          'Content-Type': 'application/json',      // <- 기본값
          'Authorization': `Bearer ${token}`        // <- 커스텀 헤더
        },
        withCredentials: true
      });
  }

}
