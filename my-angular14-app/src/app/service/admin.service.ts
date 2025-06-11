import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs"
import {AttendanceDto} from "../model/attendancedto.model";
@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private authUrl = 'http://localhost:8080/api/attendance/attendance-list';
  constructor(private http: HttpClient) { }

  getAttendanceList(): Observable<any> {
    return this.http.get<AttendanceDto[]>(this.authUrl);
  }
}
