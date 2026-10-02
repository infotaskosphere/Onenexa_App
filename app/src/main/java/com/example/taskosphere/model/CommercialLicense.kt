package com.example.taskosphere.model

data class CommercialLicense(
    val licenseKey: String,
    val companyName: String,
    val planName: String,
    val status: String,
    val validUntil: String,
    val activeUsers: Int,
    val userSeatLimit: Int,
    val enabledModules: List<String>
)
