package com.example.taskosphere.model

data class Client(
    val id: String,
    val name: String,
    val pan: String,
    val gstin: String,
    val contactPerson: String,
    val email: String,
    val phone: String,
    val city: String,
    val activeServices: List<String>
)

data class DscRecord(
    val id: String,
    val holderName: String,
    val organization: String,
    val dscClass: String, // Class 3 Signing & Encryption
    val tokenSerial: String,
    val expiryDate: String,
    val physicalLocation: String,
    val daysRemaining: Int
)

data class PassvaultCredential(
    val id: String,
    val clientName: String,
    val portalName: String, // GST Portal, Income Tax e-Filing, MCA V3, Traces, DGFT
    val portalUrl: String,
    val username: String,
    val maskedSecret: String = "••••••••••••",
    val notes: String
)

data class TrademarkRecord(
    val id: String,
    val applicationNo: String,
    val wordMark: String,
    val trademarkClass: Int,
    val applicantName: String,
    val filingDate: String,
    val status: String, // Registered, Objected, Formalities Chk Pass, Examined
    val goodsDescription: String
)
