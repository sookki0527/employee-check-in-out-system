import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs"
import {AttendanceDto} from "../model/attendancedto.model";
import {HttpHeaders} from "@angular/common/http"
@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private authUrl = 'http://localhost:8080/api/attendance/attendance-list';
  constructor(private http: HttpClient) { }

  getAttendanceList(): Observable<any> {
    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<AttendanceDto[]>(this.authUrl, {
      headers: headers,
      withCredentials: true
    });
  }
}
