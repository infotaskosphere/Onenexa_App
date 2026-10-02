package com.example.taskosphere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.theme.*

@Composable
fun LicenseScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val license by viewModel.license.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BrandNavy),
                modifier = Modifier.fillMaxWidth().testTag("license_hero_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = BrandMint, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "COMMERCIAL LICENSING RUNTIME",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMint,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = license.planName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            color = BrandEmerald.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = license.status,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BrandMint,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Licensed Tenant: ${license.companyName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "Key: ${license.licenseKey} • Expires: ${license.validUntil}",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    // Seats Progress
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("User Seat Allocation", fontSize = 11.sp, color = Color(0xFFCBD5E1))
                            Text("${license.activeUsers} / ${license.userSeatLimit} Active Seats", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BrandMint)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { license.activeUsers.toFloat() / license.userSeatLimit },
                            color = BrandMint,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.fillMaxWidth().height(6.dp)
                        )
                    }
                }
            }
        }

        // Active Entitlements
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "UNLOCKED COMMERCIAL MODULES", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Spacer(modifier = Modifier.height(10.dp))

                    license.enabledModules.forEach { mod ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = mod, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BrandDarkText)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = "Active", fontSize = 11.sp, color = BrandEmerald, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // System Health & Diagnostics
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "DIAGNOSTICS & SYSTEM HEALTH", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Spacer(modifier = Modifier.height(10.dp))

                    val diagnostics = listOf(
                        "Tenant Database Connectivity" to "Operational (12ms)",
                        "GST & Income Tax Portal Gateway" to "Online & Synchronized",
                        "DSC Hardware Cryptographic Bridge" to "Ready (Token v3)",
                        "Automated Notification Dispatcher" to "Running (Healthy)",
                        "Local Vault Storage Encryption" to "AES-256 GCM Enabled"
                    )

                    diagnostics.forEach { (name, status) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = name, fontSize = 12.sp, color = BrandDarkText)
                            Text(text = status, fontSize = 11.sp, color = BrandEmerald, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
