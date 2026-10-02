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
fun RecordsScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val clients by viewModel.clients.collectAsState()
    val dscRecords by viewModel.dscRecords.collectAsState()
    val credentials by viewModel.credentials.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Client Master", "DSC Register", "Passvault Vault")

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.showAddClientDialog.value = true },
                    containerColor = BrandNavy,
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Client") },
                    text = { Text("Add Client", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("fab_add_client")
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
                // Vault Header Banner
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
                                    Icon(Icons.Default.Security, contentDescription = null, tint = BrandMint, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "RECORDS & PASSVAULT SECURE REPOSITORY",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandMint,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                                Text(
                                    text = "Client Credentials & DSC",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Surface(
                                color = BrandEmerald.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = BrandMint, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "AES Encrypted", fontSize = 10.sp, color = BrandMint, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Centralized custody register for client Digital Signatures, token physical locations, and authorized government portal credentials.",
                            fontSize = 12.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                }
            }

            // Tabs
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
                    // Clients
                    val filtered = clients.filter {
                        searchQuery.isBlank() ||
                                it.name.contains(searchQuery, ignoreCase = true) ||
                                it.pan.contains(searchQuery, ignoreCase = true) ||
                                it.gstin.contains(searchQuery, ignoreCase = true)
                    }
                    items(filtered, key = { it.id }) { client ->
                        ClientMasterCard(client = client)
                    }
                }
                1 -> {
                    // DSC Register
                    items(dscRecords, key = { it.id }) { dsc ->
                        DscCard(dsc = dsc)
                    }
                }
                2 -> {
                    // Passvault
                    items(credentials, key = { it.id }) { cred ->
                        CredentialCard(cred = cred)
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
fun ClientMasterCard(client: Client) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().testTag("client_card_${client.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = client.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                Surface(
                    color = BrandBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = client.city,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandBlue,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "PAN: ${client.pan}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText)
                Text(text = "GSTIN: ${client.gstin}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandDarkText)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Contact: ${client.contactPerson} (${client.phone}) • ${client.email}",
                fontSize = 11.sp,
                color = BrandMutedText
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                client.activeServices.forEach { srv ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = srv,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DscCard(dsc: DscRecord) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = dsc.holderName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Text(text = dsc.organization, fontSize = 12.sp, color = BrandDarkText)
                }

                val alertColor = if (dsc.daysRemaining <= 10) StatusDanger else BrandEmerald
                Surface(
                    color = alertColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${dsc.daysRemaining} days left",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = alertColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = BrandBorder)
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = "Token Serial", fontSize = 10.sp, color = BrandMutedText)
                    Text(text = dsc.tokenSerial, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "Expiry Date", fontSize = 10.sp, color = BrandMutedText)
                    Text(text = dsc.expiryDate, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Token Location", fontSize = 10.sp, color = BrandMutedText)
                    Text(text = dsc.physicalLocation, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BrandBlue)
                }
            }
        }
    }
}

@Composable
fun CredentialCard(cred: PassvaultCredential) {
    var revealed by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = cred.portalName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BrandNavy)
                    Text(text = cred.clientName, fontSize = 12.sp, color = BrandMutedText)
                }

                IconButton(onClick = { revealed = !revealed }) {
                    Icon(
                        imageVector = if (revealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle Secret",
                        tint = BrandBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Username / User ID", fontSize = 10.sp, color = BrandMutedText)
                        Text(text = cred.username, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Encrypted Secret", fontSize = 10.sp, color = BrandMutedText)
                        Text(
                            text = if (revealed) "Auth#Secure99x!" else cred.maskedSecret,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (revealed) BrandNavy else BrandMutedText
                        )
                    }
                }
            }

            if (cred.notes.isNotBlank()) {
                Text(
                    text = "Note: ${cred.notes}",
                    fontSize = 11.sp,
                    color = BrandMutedText,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}
