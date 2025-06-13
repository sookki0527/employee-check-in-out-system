import { Component, OnInit } from '@angular/core';
import {AttendanceService} from "../service/attendance.service";
import {CheckInRequest} from "../model/CheckInRequest.model"
import {error} from "@angular/compiler-cli/src/transformers/util"
import {Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  username = localStorage.getItem('username');

  userId: number = Number(localStorage.getItem('userId') || 0);

  token!: string | null;
  constructor(private attendanceService: AttendanceService, private route: Router, private authService:AuthService) { }

  ngOnInit(): void {
    this.token = this.authService.getToken();
    console.log("Token: " + this.token);
  }
  onCheckIn() {
    const request: CheckInRequest = { userId: this.userId, timestamp: new Date().toISOString(), type: 'CHECK_IN'  };
    this.attendanceService.checkIn(request).subscribe( {
      next: () => alert('✅ Checked in successfully'),
      error: (err) => alert('❌ Check-in failed: ' + err.message)
    });
  }

  onCheckOut() {
    const request: CheckInRequest = { userId: this.userId, timestamp: new Date().toISOString(), type: 'CHECK_OUT'  };
    this.attendanceService.checkIn(request).subscribe( {
      next: () => alert('✅ Checked out successfully'),
      error: (err) => alert('❌ Check-out failed: ' + err.message)
    });
  }

  goToAdminDashBoard() {
    this.route.navigate(['/admin-dashboard']);
  }
}
