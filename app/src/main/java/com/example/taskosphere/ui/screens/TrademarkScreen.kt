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
import com.example.taskosphere.model.TrademarkRecord
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.theme.*

data class NiceClass(val classNum: Int, val title: String, val description: String)

@Composable
fun TrademarkScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val trademarks by viewModel.trademarks.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var classSearchQuery by remember { mutableStateOf("") }

    val niceClasses = remember {
        listOf(
            NiceClass(1, "Chemicals", "Chemicals for use in industry, science, photography, agriculture, and horticulture."),
            NiceClass(5, "Pharmaceuticals", "Pharmaceuticals, medical and veterinary preparations; sanitary preparations for medical purposes."),
            NiceClass(9, "Software & Electronics", "Computers, mobile applications, software, apparatus for recording or reproducing sound/images."),
            NiceClass(16, "Paper & Printed Goods", "Paper and cardboard; printed matter; bookbinding material; stationery and office requisites."),
            NiceClass(25, "Clothing & Footwear", "Clothing, footwear, headgear for human wear."),
            NiceClass(35, "Advertising & Business", "Advertising; business management, organization and administration; office functions."),
            NiceClass(36, "Finance & Insurance", "Financial, monetary and banking services; insurance services; real estate affairs."),
            NiceClass(38, "Telecommunications", "Telecommunication services; broadcasting, streaming, cellular networks."),
            NiceClass(39, "Transport & Logistics", "Transport; packaging and storage of goods; travel arrangement and freight forwarding."),
            NiceClass(41, "Education & Entertainment", "Education; providing of training; entertainment; sporting and cultural activities."),
            NiceClass(42, "Technology & IT Services", "Scientific and technological services, computer hardware/software design and cloud development."),
            NiceClass(44, "Medical & Healthcare", "Medical services; veterinary services; hygienic and beauty care for human beings or animals."),
            NiceClass(45, "Legal & Security", "Legal services; security services for physical protection of tangible property and individuals.")
        )
    }

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
                                Icon(Icons.Default.Copyright, contentDescription = null, tint = BrandMint, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "TRADEMARK SPHERE IP SUITE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandMint,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Text(
                                text = "IP & Brand Governance",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Real-time intellectual property filing status, objection defense tracking, and Nice Classification directory.",
                        fontSize = 12.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = BrandNavy,
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Active Filings (${trademarks.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Nice Class Finder", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (selectedTab == 0) {
            items(trademarks, key = { it.id }) { tm ->
                TrademarkCard(record = tm)
            }
        } else {
            item {
                OutlinedTextField(
                    value = classSearchQuery,
                    onValueChange = { classSearchQuery = it },
                    placeholder = { Text("Search class number or industry (e.g. software, medical, finance)...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_search_classes")
                )
            }

            val filteredClasses = niceClasses.filter {
                classSearchQuery.isBlank() ||
                        it.classNum.toString() == classSearchQuery.trim() ||
                        it.title.contains(classSearchQuery, ignoreCase = true) ||
                        it.description.contains(classSearchQuery, ignoreCase = true)
            }

            items(filteredClasses, key = { it.classNum }) { nc ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = BrandNavy,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Class ${nc.classNum}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = nc.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = nc.description, fontSize = 12.sp, color = BrandDarkText)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun TrademarkCard(record: TrademarkRecord) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("tm_card_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = record.wordMark, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(color = BrandBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
                        Text(
                            text = "Class ${record.trademarkClass}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                val (badgeBg, badgeFg) = when (record.status) {
                    "Registered" -> BrandEmerald.copy(alpha = 0.15f) to BrandEmerald
                    "Objected" -> StatusDanger.copy(alpha = 0.15f) to StatusDanger
                    else -> StatusWarning.copy(alpha = 0.15f) to StatusWarning
                }

                Surface(color = badgeBg, shape = RoundedCornerShape(6.dp)) {
                    Text(
                        text = record.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeFg,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "App #${record.applicationNo} • Applicant: ${record.applicantName}", fontSize = 12.sp, color = BrandDarkText, fontWeight = FontWeight.Medium)
            Text(text = "Filed: ${record.filingDate}", fontSize = 11.sp, color = BrandMutedText)

            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = record.goodsDescription,
                    fontSize = 11.sp,
                    color = BrandDarkText,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
