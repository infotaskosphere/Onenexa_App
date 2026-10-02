package com.example.taskosphere.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun FinixScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val invoices by viewModel.invoices.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val journalEntries by viewModel.journalEntries.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Sales Invoices", "Expenses & Bills", "Journal Vouchers")

    val totalBilled = invoices.sumOf { it.totalAmount }
    val totalReceived = invoices.sumOf { it.paidAmount }
    val totalReceivable = totalBilled - totalReceived
    val totalExpenseAmt = expenses.sumOf { it.amount }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.showAddInvoiceDialog.value = true },
                    containerColor = BrandNavy,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = "New Invoice") },
                    text = { Text("Create Invoice", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("fab_create_invoice")
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
                // Finix AI Accounting Header
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
                                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = BrandMint, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "FINIX AI GENERAL LEDGER",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandMint,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = "Financial Command Center",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Surface(
                                color = BrandMint.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "FY 2026-27",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMint,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Total Invoiced", fontSize = 10.sp, color = Color(0xFFCBD5E1))
                                    Text("₹${String.format("%,.0f", totalBilled)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Receivables", fontSize = 10.sp, color = Color(0xFFCBD5E1))
                                    Text("₹${String.format("%,.0f", totalReceivable)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = StatusWarning)
                                }
                            }
                            Surface(
                                color = Color.White.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Expenses", fontSize = 10.sp, color = Color(0xFFCBD5E1))
                                    Text("₹${String.format("%,.0f", totalExpenseAmt)}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BrandMint)
                                }
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
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            when (selectedTab) {
                0 -> {
                    // Invoices Tab
                    items(invoices, key = { it.id }) { inv ->
                        InvoiceCard(
                            invoice = inv,
                            onMarkPaid = { viewModel.markInvoicePaid(inv.id) }
                        )
                    }
                }
                1 -> {
                    // Expenses Tab
                    items(expenses, key = { it.id }) { exp ->
                        ExpenseCard(expense = exp)
                    }
                }
                2 -> {
                    // Journal Entries Tab
                    items(journalEntries, key = { it.id }) { je ->
                        JournalEntryCard(entry = je)
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
fun InvoiceCard(
    invoice: Invoice,
    onMarkPaid: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("invoice_item_${invoice.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = invoice.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                    Text(text = invoice.clientName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                }

                val (badgeBg, badgeFg) = when (invoice.status) {
                    InvoiceStatus.PAID -> BrandEmerald.copy(alpha = 0.15f) to BrandEmerald
                    InvoiceStatus.PARTIAL -> StatusWarning.copy(alpha = 0.15f) to StatusWarning
                    InvoiceStatus.UNPAID -> StatusDanger.copy(alpha = 0.15f) to StatusDanger
                    InvoiceStatus.OVERDUE -> Color.Red.copy(alpha = 0.15f) to Color.Red
                }

                Surface(color = badgeBg, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = invoice.status.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Line items
            invoice.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.description, fontSize = 12.sp, color = BrandDarkText, modifier = Modifier.weight(1f))
                    Text(text = "₹${String.format("%,.0f", item.amount)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
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
                    Text(text = "Due: ${invoice.dueDate} • Inc. GST 18%", fontSize = 11.sp, color = BrandMutedText)
                    Text(
                        text = "Total: ₹${String.format("%,.2f", invoice.totalAmount)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandNavy
                    )
                }

                if (invoice.status != InvoiceStatus.PAID) {
                    Button(
                        onClick = onMarkPaid,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandEmerald),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Record Payment", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandEmerald, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Fully Settled", fontSize = 12.sp, color = BrandEmerald, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
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
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    color = BrandBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = expense.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = expense.vendorName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                Text(text = "${expense.date} • Ref: ${expense.referenceNo} (${expense.paymentMode})", fontSize = 11.sp, color = BrandMutedText)
            }

            Text(
                text = "₹${String.format("%,.0f", expense.amount)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = StatusDanger
            )
        }
    }
}

@Composable
fun JournalEntryCard(entry: JournalEntry) {
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
                Text(text = entry.entryNo, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BrandBlue)
                Text(text = entry.date, fontSize = 11.sp, color = BrandMutedText)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Dr: ${entry.debitAccount}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText)
                    Text(text = "Cr: ${entry.creditAccount}", fontSize = 13.sp, color = BrandMutedText)
                }
                Text(text = "₹${String.format("%,.0f", entry.amount)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
            }

            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = entry.narration,
                    fontSize = 11.sp,
                    color = BrandDarkText,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
