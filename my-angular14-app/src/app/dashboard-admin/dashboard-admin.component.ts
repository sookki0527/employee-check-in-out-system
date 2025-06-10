import { Component, OnInit } from '@angular/core';
import {AdminService} from "../service/admin.service";
import {AttendanceDto} from "../model/attendancedto.model";
import {Router} from "@angular/router"
@Component({
  selector: 'app-dashboard-admin',
  templateUrl: './dashboard-admin.component.html',
  styleUrls: ['./dashboard-admin.component.css']
})
export class DashboardAdminComponent implements OnInit {
  attendances:AttendanceDto[] = [];
  constructor(private adminService: AdminService, private route: Router) { }

  ngOnInit(): void {
    this.getAttendances();
  }

  getAttendances(){
    this.adminService.getAttendanceList().subscribe({
      next: (data)=>{
        this.attendances = data;
        console.log('Attendance list:', data);
      },
      error: (err) => {
        console.error('Error fetching attendance list:', err);
      }
    })
  }




}
