package com.example.taskosphere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskosphere.model.*
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.theme.*

@Composable
fun PeopleMatrixScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val employees by viewModel.employees.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val leaves by viewModel.leaves.collectAsState()
    val isPunchedIn by viewModel.isPunchedIn.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Punch Log", "Team Directory", "Leave Requests", "Salary Slips")

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 2) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.showApplyLeaveDialog.value = true },
                    containerColor = BrandNavy,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Apply Leave") },
                    text = { Text("Apply Leave", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("fab_apply_leave")
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Punch Clock Hero Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandNavy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Schedule, contentDescription = null, tint = BrandMint, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "PEOPLE MATRIX IDENTIX",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandMint,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = "Smart Attendance Clock",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isPunchedIn) BrandEmerald.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = if (isPunchedIn) "PUNCHED IN" else "PUNCHED OUT",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPunchedIn) BrandMint else Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Rohan Sharma (Senior Tax Manager)", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text(text = "Shift: 09:00 AM - 06:30 PM (General)", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }

                            Button(
                                onClick = { viewModel.togglePunchClock() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPunchedIn) StatusDanger else BrandEmerald
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("btn_toggle_punch")
                            ) {
                                Icon(
                                    imageVector = if (isPunchedIn) Icons.Default.Logout else Icons.Default.Login,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isPunchedIn) "Punch Out" else "Punch In", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Sub-tabs
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = BrandNavy,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // Punch History Log
                    items(attendance, key = { it.id }) { rec ->
                        AttendanceCard(record = rec)
                    }
                }
                1 -> {
                    // Team Directory
                    items(employees, key = { it.id }) { emp ->
                        EmployeeCard(employee = emp)
                    }
                }
                2 -> {
                    // Leave Requests
                    items(leaves, key = { it.id }) { lv ->
                        LeaveCard(leave = lv)
                    }
                }
                3 -> {
                    // Salary Slips Breakdown
                    items(employees, key = { it.id }) { emp ->
                        SalarySlipCard(employee = emp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun AttendanceCard(record: AttendanceRecord) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = record.date, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                Text(
                    text = "In: ${record.checkIn} • Out: ${record.checkOut ?: "Active"}",
                    fontSize = 12.sp,
                    color = BrandDarkText
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (record.durationHours > 0) {
                    Text(
                        text = "${record.durationHours} hrs",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandMutedText,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Surface(
                    color = if (record.status == AttendanceStatus.PRESENT) BrandEmerald.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = record.status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (record.status == AttendanceStatus.PRESENT) BrandEmerald else StatusWarning,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeCard(employee: Employee) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(BrandNavy),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = employee.name.take(2).uppercase(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = employee.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = employee.empCode, fontSize = 11.sp, color = BrandMutedText)
                }
                Text(text = "${employee.designation} • ${employee.department}", fontSize = 12.sp, color = BrandDarkText)
                Text(text = "${employee.email} • ${employee.phone}", fontSize = 11.sp, color = BrandMutedText)
            }
        }
    }
}

@Composable
fun LeaveCard(leave: LeaveApplication) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = leave.employeeName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Text(text = "${leave.leaveType} (${leave.daysCount} days)", fontSize = 12.sp, color = BrandBlue, fontWeight = FontWeight.SemiBold)
                }

                Surface(
                    color = if (leave.status == "Approved") BrandEmerald.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = leave.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (leave.status == "Approved") BrandEmerald else StatusWarning,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "${leave.startDate} to ${leave.endDate}",
                fontSize = 12.sp,
                color = BrandDarkText,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Reason: ${leave.reason}",
                fontSize = 11.sp,
                color = BrandMutedText,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SalarySlipCard(employee: Employee) {
    val basic = employee.monthlyGross * 0.50
    val hra = employee.monthlyGross * 0.30
    val special = employee.monthlyGross * 0.20
    val pf = basic * 0.12
    val tds = employee.monthlyGross * 0.05
    val net = employee.monthlyGross - pf - tds

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = employee.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Text(text = "Pay Month: September 2026", fontSize = 11.sp, color = BrandMutedText)
                }
                Text(
                    text = "Net: ₹${String.format("%,.0f", net)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandEmerald
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = BrandBorder)
            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Basic: ₹${String.format("%,.0f", basic)}", fontSize = 11.sp, color = BrandDarkText)
                Text(text = "HRA: ₹${String.format("%,.0f", hra)}", fontSize = 11.sp, color = BrandDarkText)
                Text(text = "Special: ₹${String.format("%,.0f", special)}", fontSize = 11.sp, color = BrandDarkText)
            }

            Row(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Deductions: PF ₹${String.format("%,.0f", pf)} • TDS ₹${String.format("%,.0f", tds)}", fontSize = 11.sp, color = StatusDanger)
                Text(text = "Gross: ₹${String.format("%,.0f", employee.monthlyGross)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
