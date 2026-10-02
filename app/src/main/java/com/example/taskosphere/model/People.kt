package com.example.taskosphere.model

enum class AttendanceStatus(val label: String) {
    PRESENT("Present"),
    ON_DUTY("On Duty"),
    HALF_DAY("Half Day"),
    LEAVE("On Leave"),
    ABSENT("Absent")
}

data class Employee(
    val id: String,
    val empCode: String,
    val name: String,
    val designation: String,
    val department: String,
    val email: String,
    val phone: String,
    val monthlyGross: Double,
    val isActive: Boolean = true
)

data class AttendanceRecord(
    val id: String,
    val date: String,
    val checkIn: String,
    val checkOut: String?,
    val durationHours: Double,
    val status: AttendanceStatus
)

data class LeaveApplication(
    val id: String,
    val employeeName: String,
    val leaveType: String, // Casual, Sick, Earned
    val startDate: String,
    val endDate: String,
    val daysCount: Int,
    val reason: String,
    val status: String // Approved, Pending, Rejected
)

data class SalarySlip(
    val id: String,
    val employeeName: String,
    val monthYear: String,
    val basic: Double,
    val hra: Double,
    val specialAllowance: Double,
    val pfDeduction: Double,
    val taxDeduction: Double,
    val netSalary: Double
)
