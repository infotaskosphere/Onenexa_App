package com.example.taskosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskosphere.ui.AppModule
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.components.*
import com.example.taskosphere.ui.screens.*
import com.example.taskosphere.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskosphereTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: TaskosphereViewModel = viewModel()
) {
    val currentModule by viewModel.currentModule.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isPunchedIn by viewModel.isPunchedIn.collectAsState()

    val showAddTask by viewModel.showAddTaskDialog.collectAsState()
    val showAddInvoice by viewModel.showAddInvoiceDialog.collectAsState()
    val showApplyLeave by viewModel.showApplyLeaveDialog.collectAsState()
    val showAddClient by viewModel.showAddClientDialog.collectAsState()
    val complianceToFile by viewModel.showFileComplianceDialog.collectAsState()

    Scaffold(
        topBar = {
            SuiteTopBar(
                currentModule = currentModule,
                onSelectModule = { viewModel.selectModule(it) },
                searchQuery = searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                isPunchedIn = isPunchedIn,
                onTogglePunch = { viewModel.togglePunchClock() }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val primaryModules = listOf(
                    AppModule.TASKOSPHERE to Icons.Default.Assignment,
                    AppModule.COMPLIGENIE to Icons.Default.Verified,
                    AppModule.FINIX_AI to Icons.Default.AccountBalance,
                    AppModule.PEOPLE_MATRIX to Icons.Default.Group,
                    AppModule.VAULT_RECORDS to Icons.Default.VpnKey
                )

                primaryModules.forEach { (module, icon) ->
                    val isSelected = currentModule == module
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectModule(module) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = module.title,
                                tint = if (isSelected) BrandNavy else BrandMutedText
                            )
                        },
                        label = {
                            Text(
                                text = module.title.split(" ").first(),
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BrandNavy,
                            selectedTextColor = BrandNavy,
                            indicatorColor = BrandSky
                        ),
                        modifier = Modifier.testTag("nav_bottom_${module.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentModule) {
                AppModule.TASKOSPHERE -> TasksScreen(viewModel = viewModel)
                AppModule.COMPLIGENIE -> ComplianceScreen(viewModel = viewModel)
                AppModule.FINIX_AI -> FinixScreen(viewModel = viewModel)
                AppModule.PEOPLE_MATRIX -> PeopleMatrixScreen(viewModel = viewModel)
                AppModule.VAULT_RECORDS -> RecordsScreen(viewModel = viewModel)
                AppModule.TRADEMARK -> TrademarkScreen(viewModel = viewModel)
                AppModule.LICENSE -> LicenseScreen(viewModel = viewModel)
            }
        }
    }

    // Modal Dialogs
    if (showAddTask) {
        AddTaskDialog(
            onDismiss = { viewModel.showAddTaskDialog.value = false },
            onConfirm = { title, client, assignedTo, priority, category, dueDate, notes ->
                viewModel.addTask(title, client, assignedTo, priority, category, dueDate, notes)
            }
        )
    }

    if (showAddInvoice) {
        AddInvoiceDialog(
            onDismiss = { viewModel.showAddInvoiceDialog.value = false },
            onConfirm = { clientName, description, amount, dueDate ->
                viewModel.addInvoice(clientName, description, amount, dueDate)
            }
        )
    }

    if (showApplyLeave) {
        ApplyLeaveDialog(
            onDismiss = { viewModel.showApplyLeaveDialog.value = false },
            onConfirm = { leaveType, start, end, days, reason ->
                viewModel.applyLeave(leaveType, start, end, days, reason)
            }
        )
    }

    if (showAddClient) {
        AddClientDialog(
            onDismiss = { viewModel.showAddClientDialog.value = false },
            onConfirm = { name, pan, gstin, contactPerson, phone, email, city ->
                viewModel.addClient(name, pan, gstin, contactPerson, phone, email, city)
            }
        )
    }

    complianceToFile?.let { item ->
        FileComplianceDialog(
            item = item,
            onDismiss = { viewModel.showFileComplianceDialog.value = null },
            onConfirm = { ackNo ->
                viewModel.markComplianceFiled(item.id, ackNo)
                viewModel.showFileComplianceDialog.value = null
            }
        )
    }
}
