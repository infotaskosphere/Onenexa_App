package com.example.taskosphere.model

enum class InvoiceStatus(val label: String) {
    PAID("Paid"),
    PARTIAL("Partially Paid"),
    UNPAID("Unpaid"),
    OVERDUE("Overdue")
}

data class InvoiceItem(
    val description: String,
    val quantity: Int = 1,
    val unitRate: Double,
    val amount: Double = quantity * unitRate
)

data class Invoice(
    val id: String,
    val invoiceNumber: String,
    val clientName: String,
    val issueDate: String,
    val dueDate: String,
    val items: List<InvoiceItem>,
    val taxRatePercent: Double = 18.0,
    val subtotal: Double,
    val totalAmount: Double,
    val paidAmount: Double,
    val status: InvoiceStatus
)

data class Expense(
    val id: String,
    val vendorName: String,
    val category: String,
    val date: String,
    val amount: Double,
    val paymentMode: String = "Bank Transfer",
    val referenceNo: String
)

data class JournalEntry(
    val id: String,
    val entryNo: String,
    val date: String,
    val debitAccount: String,
    val creditAccount: String,
    val amount: Double,
    val narration: String
)

data class AccountSummary(
    val code: String,
    val accountName: String,
    val category: String, // Asset, Liability, Equity, Revenue, Expense
    val balance: Double
)
