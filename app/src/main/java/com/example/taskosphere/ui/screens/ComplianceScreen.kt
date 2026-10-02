package com.example.taskosphere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.taskosphere.model.*
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.theme.*

@Composable
fun ComplianceScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.complianceItems.collectAsState()
    val selectedCategory by viewModel.complianceCategoryFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredItems = items.filter { item ->
        val matchesCategory = selectedCategory == null || item.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                item.formName.contains(searchQuery, ignoreCase = true) ||
                item.actName.contains(searchQuery, ignoreCase = true) ||
                item.period.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    val filedCount = items.count { it.status == FilingStatus.FILED }
    val totalCount = items.size
    val complianceRate = if (totalCount > 0) (filedCount * 100) / totalCount else 100

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // CompliGenie Hero Banner
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
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = BrandMint, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "COMPLIGENIE AUDIT ENGINE",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMint,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "Statutory Filing Tracker",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = "$complianceRate%", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BrandMint)
                                Text(text = "Health Score", fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Automated statutory compliance tracking for GST returns, Income Tax TDS, Advance Tax schedules, and Ministry of Corporate Affairs annual filing mandates.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandMint, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "$filedCount Filed", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Alarm, contentDescription = null, tint = StatusWarning, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "${totalCount - filedCount} Pending", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { viewModel.setComplianceCategoryFilter(null) },
                    label = { Text("All Mandates (${items.size})", fontSize = 11.sp) }
                )
                ComplianceCategory.values().forEach { cat ->
                    val count = items.count { it.category == cat }
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { viewModel.setComplianceCategoryFilter(cat) },
                        label = { Text("${cat.displayName} ($count)", fontSize = 11.sp) }
                    )
                }
            }
        }

        // List of Compliance Items
        items(filteredItems, key = { it.id }) { item ->
            ComplianceCard(
                item = item,
                onFileClick = { viewModel.showFileComplianceDialog.value = item }
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ComplianceCard(
    item: ComplianceItem,
    onFileClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("compliance_item_${item.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (item.category) {
                        ComplianceCategory.GST -> Icons.Default.ReceiptLong
                        ComplianceCategory.INCOME_TAX -> Icons.Default.AccountBalanceWallet
                        ComplianceCategory.ROC_MCA -> Icons.Default.Gavel
                        ComplianceCategory.TDS -> Icons.Default.Percent
                    }
                    Icon(icon, contentDescription = null, tint = BrandBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.formName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandNavy
                    )
                }

                val (badgeBg, badgeFg) = when (item.status) {
                    FilingStatus.FILED -> BrandEmerald.copy(alpha = 0.15f) to BrandEmerald
                    FilingStatus.DUE_SOON -> StatusWarning.copy(alpha = 0.15f) to StatusWarning
                    FilingStatus.OVERDUE -> StatusDanger.copy(alpha = 0.15f) to StatusDanger
                    FilingStatus.UPCOMING -> BrandBlue.copy(alpha = 0.15f) to BrandBlue
                }

                Surface(color = badgeBg, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = item.status.displayName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = "${item.actName} • Period: ${item.period}",
                fontSize = 12.sp,
                color = BrandMutedText,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (item.penaltyAlert != null) {
                Surface(
                    color = StatusDanger.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = StatusDanger, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = item.penaltyAlert, fontSize = 11.sp, color = StatusDanger, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = BrandBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Statutory Due Date", fontSize = 10.sp, color = BrandMutedText)
                    Text(text = item.dueDate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BrandDarkText)
                }

                if (item.status == FilingStatus.FILED) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Filed on: ${item.filedDate}", fontSize = 11.sp, color = BrandEmerald, fontWeight = FontWeight.SemiBold)
                        Text(text = "Ack: ${item.acknowledgmentNo}", fontSize = 10.sp, color = BrandMutedText)
                    }
                } else {
                    Button(
                        onClick = onFileClick,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandNavy),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Record Filing", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
