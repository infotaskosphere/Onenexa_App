package com.example.taskosphere.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskosphere.data.TaskosphereRepository
import com.example.taskosphere.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppModule(val title: String, val subtitle: String) {
    TASKOSPHERE("Taskosphere", "Tasks & Workflow"),
    COMPLIGENIE("CompliGenie", "Statutory Compliance"),
    FINIX_AI("Finix AI", "Smart Accounting"),
    PEOPLE_MATRIX("People Matrix", "HR & Attendance"),
    VAULT_RECORDS("Vault & Records", "Clients & Passwords"),
    TRADEMARK("Trademark", "IP & Classes"),
    LICENSE("License", "Commercial Console")
}

class TaskosphereViewModel(
    val repository: TaskosphereRepository = TaskosphereRepository()
) : ViewModel() {

    private val _currentModule = MutableStateFlow(AppModule.TASKOSPHERE)
    val currentModule: StateFlow<AppModule> = _currentModule.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Tasks filter state
    private val _taskStatusFilter = MutableStateFlow<TaskStatus?>(null)
    val taskStatusFilter: StateFlow<TaskStatus?> = _taskStatusFilter.asStateFlow()

    private val _taskCategoryFilter = MutableStateFlow<TaskCategory?>(null)
    val taskCategoryFilter: StateFlow<TaskCategory?> = _taskCategoryFilter.asStateFlow()

    // Compliance filter state
    private val _complianceCategoryFilter = MutableStateFlow<ComplianceCategory?>(null)
    val complianceCategoryFilter: StateFlow<ComplianceCategory?> = _complianceCategoryFilter.asStateFlow()

    // Passvault search
    private val _vaultSearchQuery = MutableStateFlow("")
    val vaultSearchQuery: StateFlow<String> = _vaultSearchQuery.asStateFlow()

    // Active Dialog Flags
    var showAddTaskDialog = MutableStateFlow(false)
    var showAddInvoiceDialog = MutableStateFlow(false)
    var showApplyLeaveDialog = MutableStateFlow(false)
    var showAddClientDialog = MutableStateFlow(false)
    var showFileComplianceDialog = MutableStateFlow<ComplianceItem?>(null)

    // Data streams
    val tasks = repository.tasks
    val todos = repository.todos
    val complianceItems = repository.complianceItems
    val invoices = repository.invoices
    val expenses = repository.expenses
    val journalEntries = repository.journalEntries
    val employees = repository.employees
    val attendance = repository.attendance
    val leaves = repository.leaves
    val clients = repository.clients
    val dscRecords = repository.dscRecords
    val credentials = repository.credentials
    val trademarks = repository.trademarks
    val license = repository.license
    val isPunchedIn = repository.isPunchedIn

    // Filtered tasks
    val filteredTasks = combine(tasks, _taskStatusFilter, _taskCategoryFilter, _searchQuery) { taskList, status, category, query ->
        taskList.filter { task ->
            val matchesStatus = status == null || task.status == status
            val matchesCategory = category == null || task.category == category
            val matchesQuery = query.isBlank() ||
                    task.title.contains(query, ignoreCase = true) ||
                    task.clientName.contains(query, ignoreCase = true) ||
                    task.assignedTo.contains(query, ignoreCase = true)
            matchesStatus && matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectModule(module: AppModule) {
        _currentModule.value = module
        _searchQuery.value = ""
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setTaskStatusFilter(status: TaskStatus?) {
        _taskStatusFilter.value = status
    }

    fun setTaskCategoryFilter(category: TaskCategory?) {
        _taskCategoryFilter.value = category
    }

    fun setComplianceCategoryFilter(cat: ComplianceCategory?) {
        _complianceCategoryFilter.value = cat
    }

    fun setVaultSearchQuery(q: String) {
        _vaultSearchQuery.value = q
    }

    // Repository Delegation
    fun addTask(
        title: String,
        client: String,
        assignedTo: String,
        priority: TaskPriority,
        category: TaskCategory,
        dueDate: String,
        notes: String
    ) {
        val newTask = Task(
            id = "TSK-${System.currentTimeMillis() % 10000}",
            title = title,
            clientName = client,
            assignedTo = assignedTo,
            priority = priority,
            status = TaskStatus.PENDING,
            category = category,
            dueDate = dueDate,
            notes = notes
        )
        repository.addTask(newTask)
    }

    fun updateTaskStatus(taskId: String, status: TaskStatus) {
        repository.updateTaskStatus(taskId, status)
    }

    fun deleteTask(taskId: String) {
        repository.deleteTask(taskId)
    }

    fun toggleTodo(todoId: String) {
        repository.toggleTodo(todoId)
    }

    fun addTodo(title: String, category: String) {
        repository.addTodo(title, category)
    }

    fun deleteTodo(todoId: String) {
        repository.deleteTodo(todoId)
    }

    fun markComplianceFiled(id: String, ackNo: String) {
        repository.markComplianceFiled(id, ackNo)
    }

    fun addInvoice(
        clientName: String,
        description: String,
        amount: Double,
        dueDate: String
    ) {
        val tax = amount * 0.18
        val total = amount + tax
        val invoice = Invoice(
            id = UUID.randomUUID().toString(),
            invoiceNumber = "APX/26-27/${(100..999).random()}",
            clientName = clientName,
            issueDate = "02 Oct 2026",
            dueDate = dueDate,
            items = listOf(InvoiceItem(description = description, unitRate = amount, amount = amount)),
            taxRatePercent = 18.0,
            subtotal = amount,
            totalAmount = total,
            paidAmount = 0.0,
            status = InvoiceStatus.UNPAID
        )
        repository.addInvoice(invoice)
    }

    fun markInvoicePaid(id: String) {
        repository.markInvoicePaid(id)
    }

    fun togglePunchClock() {
        repository.togglePunchClock()
    }

    fun applyLeave(leaveType: String, start: String, end: String, days: Int, reason: String) {
        val leave = LeaveApplication(
            id = "LV-${(10..99).random()}",
            employeeName = "Rohan Sharma",
            leaveType = leaveType,
            startDate = start,
            endDate = end,
            daysCount = days,
            reason = reason,
            status = "Pending"
        )
        repository.applyLeave(leave)
    }

    fun addClient(name: String, pan: String, gstin: String, contactPerson: String, phone: String, email: String, city: String) {
        val client = Client(
            id = "CL-${(10..99).random()}",
            name = name,
            pan = pan.uppercase(),
            gstin = gstin.uppercase(),
            contactPerson = contactPerson,
            email = email,
            phone = phone,
            city = city,
            activeServices = listOf("GST Compliance", "Income Tax")
        )
        repository.addClient(client)
    }
}
