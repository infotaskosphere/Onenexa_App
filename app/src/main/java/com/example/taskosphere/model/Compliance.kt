package com.example.taskosphere.model

enum class ComplianceCategory(val displayName: String) {
    GST("GST Filings"),
    INCOME_TAX("Income Tax"),
    ROC_MCA("ROC / MCA"),
    TDS("TDS / TCS")
}

enum class FilingStatus(val displayName: String) {
    UPCOMING("Upcoming"),
    DUE_SOON("Due Soon"),
    OVERDUE("Overdue"),
    FILED("Filed")
}

data class ComplianceItem(
    val id: String,
    val formName: String,
    val actName: String,
    val period: String,
    val dueDate: String,
    val category: ComplianceCategory,
    val status: FilingStatus,
    val acknowledgmentNo: String? = null,
    val filedDate: String? = null,
    val penaltyAlert: String? = null
)
