package com.example.taskosphere.model

enum class TaskPriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent")
}

enum class TaskStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    REVIEW("In Review"),
    COMPLETED("Completed")
}

enum class TaskCategory(val label: String) {
    TAX("Taxation"),
    AUDIT("Audit & Assurance"),
    LEGAL("Legal Matters"),
    MCA("ROC / MCA"),
    COMPLIANCE("Statutory"),
    GENERAL("General")
}

data class Task(
    val id: String,
    val title: String,
    val clientName: String,
    val assignedTo: String,
    val priority: TaskPriority,
    val status: TaskStatus,
    val category: TaskCategory,
    val dueDate: String,
    val estimatedHours: Double = 2.0,
    val notes: String = ""
)

data class TodoItem(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false,
    val category: String = "General"
)
