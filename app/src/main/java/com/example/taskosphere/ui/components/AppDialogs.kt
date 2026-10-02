package com.example.taskosphere.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskosphere.model.*
import com.example.taskosphere.ui.theme.*

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, client: String, assignedTo: String, priority: TaskPriority, category: TaskCategory, dueDate: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var client by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.HIGH) }
    var category by remember { mutableStateOf(TaskCategory.TAX) }
    var dueDate by remember { mutableStateOf("25 Oct 2026") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Create New Task",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_task_title")
                )

                OutlinedTextField(
                    value = client,
                    onValueChange = { client = it },
                    label = { Text("Client Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_task_client")
                )

                OutlinedTextField(
                    value = assignedTo,
                    onValueChange = { assignedTo = it },
                    label = { Text("Assignee Staff *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_task_assignee")
                )

                // Priority Selection
                Text("Priority", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TaskPriority.values().forEach { prio ->
                        val isSelected = prio == priority
                        FilterChip(
                            selected = isSelected,
                            onClick = { priority = prio },
                            label = { Text(prio.label, fontSize = 11.sp) }
                        )
                    }
                }

                // Category Selection
                Text("Category", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(TaskCategory.TAX, TaskCategory.AUDIT, TaskCategory.MCA, TaskCategory.LEGAL).forEach { cat ->
                        val isSelected = cat == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { category = cat },
                            label = { Text(cat.label, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Scope & Notes") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && client.isNotBlank()) {
                                onConfirm(title, client, assignedTo.ifBlank { "Unassigned" }, priority, category, dueDate, notes)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy),
                        modifier = Modifier.testTag("btn_submit_task")
                    ) {
                        Text("Save Task")
                    }
                }
            }
        }
    }
}

@Composable
fun AddInvoiceDialog(
    onDismiss: () -> Unit,
    onConfirm: (clientName: String, description: String, amount: Double, dueDate: String) -> Unit
) {
    var clientName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("Professional Retainer Services") }
    var amountStr by remember { mutableStateOf("25000") }
    var dueDate by remember { mutableStateOf("25 Oct 2026") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Generate Tax Invoice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )

                OutlinedTextField(
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("Billed To Client *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Service") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Subtotal (INR)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Payment Due Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                val amount = amountStr.toDoubleOrNull() ?: 0.0
                val gst = amount * 0.18
                val total = amount + gst

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal:", fontSize = 12.sp)
                            Text("₹${String.format("%,.2f", amount)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (18%):", fontSize = 12.sp)
                            Text("₹${String.format("%,.2f", gst)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Divider(modifier = Modifier.padding(vertical = 4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Invoice:", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("₹${String.format("%,.2f", total)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (clientName.isNotBlank() && amount > 0) {
                                onConfirm(clientName, description, amount, dueDate)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                    ) {
                        Text("Create Invoice")
                    }
                }
            }
        }
    }
}

@Composable
fun ApplyLeaveDialog(
    onDismiss: () -> Unit,
    onConfirm: (leaveType: String, start: String, end: String, days: Int, reason: String) -> Unit
) {
    var leaveType by remember { mutableStateOf("Casual Leave") }
    var startDate by remember { mutableStateOf("15 Oct 2026") }
    var endDate by remember { mutableStateOf("16 Oct 2026") }
    var daysCount by remember { mutableStateOf("2") }
    var reason by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Apply for Leave",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Casual Leave", "Sick Leave", "Earned Leave").forEach { type ->
                        FilterChip(
                            selected = leaveType == type,
                            onClick = { leaveType = type },
                            label = { Text(type, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for Leave *") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (reason.isNotBlank()) {
                                onConfirm(leaveType, startDate, endDate, daysCount.toIntOrNull() ?: 1, reason)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                    ) {
                        Text("Submit Request")
                    }
                }
            }
        }
    }
}

@Composable
fun AddClientDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, pan: String, gstin: String, contactPerson: String, phone: String, email: String, city: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var pan by remember { mutableStateOf("") }
    var gstin by remember { mutableStateOf("") }
    var contactPerson by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Add Corporate Client",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Entity / Company Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = pan,
                        onValueChange = { pan = it },
                        label = { Text("PAN Number") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gstin,
                        onValueChange = { gstin = it },
                        label = { Text("GSTIN") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = contactPerson,
                    onValueChange = { contactPerson = it },
                    label = { Text("Primary Contact Person") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Official Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onConfirm(name, pan, gstin, contactPerson, phone, email, city)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                    ) {
                        Text("Save Client")
                    }
                }
            }
        }
    }
}

@Composable
fun FileComplianceDialog(
    item: ComplianceItem,
    onDismiss: () -> Unit,
    onConfirm: (ackNo: String) -> Unit
) {
    var ackNo by remember { mutableStateOf("ARN-GST-${(100000..999999).random()}") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Record Statutory Filing", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Form: ${item.formName} (${item.actName})", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text("Period: ${item.period} | Due: ${item.dueDate}", fontSize = 12.sp, color = BrandMutedText)

                OutlinedTextField(
                    value = ackNo,
                    onValueChange = { ackNo = it },
                    label = { Text("Portal Acknowledgment / SRN / Challan No *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (ackNo.isNotBlank()) {
                        onConfirm(ackNo)
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald)
            ) {
                Text("Confirm Filing")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
