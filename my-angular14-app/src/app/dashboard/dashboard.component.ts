import { Component, OnInit } from '@angular/core';
import {AttendanceService} from "../service/attendance.service";
import {CheckInRequest} from "../model/CheckInRequest.model"
import {error} from "@angular/compiler-cli/src/transformers/util"
import {Router} from "@angular/router";
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userId: number = 1;


  constructor(private attendanceService: AttendanceService, private route: Router) { }

  ngOnInit(): void {
    // const id = localStorage.getItem('id');
    // this.userId = id ? Number(id) : 0;

  }
  onCheckIn() {
    const request: CheckInRequest = { userId: this.userId, timestamp: new Date(), type: 'CHECK_IN'  };
    this.attendanceService.checkIn(request).subscribe( {
      next: () => alert('✅ Checked in successfully'),
      error: (err) => alert('❌ Check-in failed: ' + err.message)
    });
  }

  onCheckOut() {
    const request: CheckInRequest = { userId: this.userId, timestamp: new Date(), type: 'CHECK_OUT'  };
    this.attendanceService.checkIn(request).subscribe( {
      next: () => alert('✅ Checked out successfully'),
      error: (err) => alert('❌ Check-out failed: ' + err.message)
    });
  }

  goToAdminDashBoard() {
    this.route.navigate(['/admin-dashboard']);
  }
}
