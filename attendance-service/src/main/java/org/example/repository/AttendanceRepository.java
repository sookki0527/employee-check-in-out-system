package org.example.repository;

import org.example.entity.Attendance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {


}
