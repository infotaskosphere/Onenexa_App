package com.example.taskosphere.data

import com.example.taskosphere.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TaskosphereRepository {

    private val _tasks = MutableStateFlow<List<Task>>(initialTasks())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _todos = MutableStateFlow<List<TodoItem>>(initialTodos())
    val todos: StateFlow<List<TodoItem>> = _todos.asStateFlow()

    private val _complianceItems = MutableStateFlow<List<ComplianceItem>>(initialCompliance())
    val complianceItems: StateFlow<List<ComplianceItem>> = _complianceItems.asStateFlow()

    private val _invoices = MutableStateFlow<List<Invoice>>(initialInvoices())
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(initialExpenses())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _journalEntries = MutableStateFlow<List<JournalEntry>>(initialJournalEntries())
    val journalEntries: StateFlow<List<JournalEntry>> = _journalEntries.asStateFlow()

    private val _employees = MutableStateFlow<List<Employee>>(initialEmployees())
    val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

    private val _attendance = MutableStateFlow<List<AttendanceRecord>>(initialAttendance())
    val attendance: StateFlow<List<AttendanceRecord>> = _attendance.asStateFlow()

    private val _leaves = MutableStateFlow<List<LeaveApplication>>(initialLeaves())
    val leaves: StateFlow<List<LeaveApplication>> = _leaves.asStateFlow()

    private val _clients = MutableStateFlow<List<Client>>(initialClients())
    val clients: StateFlow<List<Client>> = _clients.asStateFlow()

    private val _dscRecords = MutableStateFlow<List<DscRecord>>(initialDscRecords())
    val dscRecords: StateFlow<List<DscRecord>> = _dscRecords.asStateFlow()

    private val _credentials = MutableStateFlow<List<PassvaultCredential>>(initialCredentials())
    val credentials: StateFlow<List<PassvaultCredential>> = _credentials.asStateFlow()

    private val _trademarks = MutableStateFlow<List<TrademarkRecord>>(initialTrademarks())
    val trademarks: StateFlow<List<TrademarkRecord>> = _trademarks.asStateFlow()

    private val _license = MutableStateFlow(
        CommercialLicense(
            licenseKey = "TSKO-ENT-2026-9941X-IND",
            companyName = "Apex Corporate Advisory LLP",
            planName = "Enterprise Commercial Multi-Tenant",
            status = "Active & Verified",
            validUntil = "31 Dec 2026",
            activeUsers = 18,
            userSeatLimit = 50,
            enabledModules = listOf("Taskosphere Core", "CompliGenie", "Finix AI", "People Matrix", "Records & DSC Vault", "Trademark Sphere", "Client Portal")
        )
    )
    val license: StateFlow<CommercialLicense> = _license.asStateFlow()

    // Current punch state for user
    private val _isPunchedIn = MutableStateFlow(true)
    val isPunchedIn: StateFlow<Boolean> = _isPunchedIn.asStateFlow()

    // ── Task Actions ──
    fun addTask(task: Task) {
        _tasks.value = listOf(task) + _tasks.value
    }

    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(status = newStatus) else it
        }
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    // ── Todo Actions ──
    fun toggleTodo(todoId: String) {
        _todos.value = _todos.value.map {
            if (it.id == taskIdOrTodo(it.id, todoId)) it.copy(isCompleted = !it.isCompleted) else it
        }
    }

    private fun taskIdOrTodo(id: String, target: String) = if (id == target) target else ""

    fun addTodo(title: String, category: String = "General") {
        val newTodo = TodoItem(id = UUID.randomUUID().toString(), title = title, category = category)
        _todos.value = listOf(newTodo) + _todos.value
    }

    fun deleteTodo(todoId: String) {
        _todos.value = _todos.value.filter { it.id != todoId }
    }

    // ── Compliance Actions ──
    fun markComplianceFiled(id: String, ackNo: String) {
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        _complianceItems.value = _complianceItems.value.map {
            if (it.id == id) {
                it.copy(
                    status = FilingStatus.FILED,
                    acknowledgmentNo = ackNo,
                    filedDate = today,
                    penaltyAlert = null
                )
            } else it
        }
    }

    fun addComplianceItem(item: ComplianceItem) {
        _complianceItems.value = listOf(item) + _complianceItems.value
    }

    // ── Accounting Actions ──
    fun addInvoice(invoice: Invoice) {
        _invoices.value = listOf(invoice) + _invoices.value
    }

    fun markInvoicePaid(invoiceId: String) {
        _invoices.value = _invoices.value.map {
            if (it.id == invoiceId) it.copy(status = InvoiceStatus.PAID, paidAmount = it.totalAmount) else it
        }
    }

    fun addExpense(expense: Expense) {
        _expenses.value = listOf(expense) + _expenses.value
    }

    fun addJournalEntry(entry: JournalEntry) {
        _journalEntries.value = listOf(entry) + _journalEntries.value
    }

    // ── People Matrix Actions ──
    fun togglePunchClock() {
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
        if (_isPunchedIn.value) {
            // Punch Out
            _isPunchedIn.value = false
            _attendance.value = _attendance.value.mapIndexed { index, record ->
                if (index == 0 && record.checkOut == null) {
                    record.copy(checkOut = now, durationHours = 8.5)
                } else record
            }
        } else {
            // Punch In
            _isPunchedIn.value = true
            val newRecord = AttendanceRecord(
                id = UUID.randomUUID().toString(),
                date = today,
                checkIn = now,
                checkOut = null,
                durationHours = 0.0,
                status = AttendanceStatus.PRESENT
            )
            _attendance.value = listOf(newRecord) + _attendance.value
        }
    }

    fun applyLeave(leave: LeaveApplication) {
        _leaves.value = listOf(leave) + _leaves.value
    }

    fun addEmployee(employee: Employee) {
        _employees.value = listOf(employee) + _employees.value
    }

    // ── Records & Vault Actions ──
    fun addClient(client: Client) {
        _clients.value = listOf(client) + _clients.value
    }

    fun addDsc(record: DscRecord) {
        _dscRecords.value = listOf(record) + _dscRecords.value
    }

    fun addCredential(cred: PassvaultCredential) {
        _credentials.value = listOf(cred) + _credentials.value
    }

    fun addTrademark(tm: TrademarkRecord) {
        _trademarks.value = listOf(tm) + _trademarks.value
    }

    companion object {
        private fun initialTasks(): List<Task> = listOf(
            Task(
                id = "TSK-101",
                title = "GSTR-3B Monthly Return Filing",
                clientName = "Nexus Infotech Solutions Pvt Ltd",
                assignedTo = "Rohan Sharma",
                priority = TaskPriority.HIGH,
                status = TaskStatus.IN_PROGRESS,
                category = TaskCategory.TAX,
                dueDate = "20 Oct 2026",
                estimatedHours = 3.5,
                notes = "Reconcile GSTR-2B purchase input tax credits prior to generating liability draft."
            ),
            Task(
                id = "TSK-102",
                title = "Statutory Audit Working Papers Review",
                clientName = "Horizon Logistics Global",
                assignedTo = "Pooja Verma",
                priority = TaskPriority.URGENT,
                status = TaskStatus.PENDING,
                category = TaskCategory.AUDIT,
                dueDate = "15 Oct 2026",
                estimatedHours = 6.0,
                notes = "Verify fixed asset register and bank confirmation statements for Q2 closing."
            ),
            Task(
                id = "TSK-103",
                title = "TDS Quarterly Statement (Form 26Q) Q2",
                clientName = "Zenith Healthcare & Diagnostics",
                assignedTo = "Amit Patel",
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.REVIEW,
                category = TaskCategory.COMPLIANCE,
                dueDate = "31 Oct 2026",
                estimatedHours = 2.5,
                notes = "Section 194C contractor deductions verified against Challan 281 receipts."
            ),
            Task(
                id = "TSK-104",
                title = "ROC Form AOC-4 Financials Filing",
                clientName = "Matrix Cloud Networks Pvt Ltd",
                assignedTo = "Kavita Rao",
                priority = TaskPriority.HIGH,
                status = TaskStatus.PENDING,
                category = TaskCategory.MCA,
                dueDate = "29 Oct 2026",
                estimatedHours = 4.0,
                notes = "Attach signed Director's report, MGT-9 extract and standalone balance sheet."
            ),
            Task(
                id = "TSK-105",
                title = "Trademark Examination Reply Drafting (Class 42)",
                clientName = "OmniTech Software Labs",
                assignedTo = "Adv. Siddharth Joshi",
                priority = TaskPriority.MEDIUM,
                status = TaskStatus.IN_PROGRESS,
                category = TaskCategory.LEGAL,
                dueDate = "18 Oct 2026",
                estimatedHours = 3.0,
                notes = "Address Section 11 relative objection citing prior registered phonetically similar mark."
            ),
            Task(
                id = "TSK-106",
                title = "Monthly Client Payroll & Salary Slip Distribution",
                clientName = "BlueStar Retail Chains",
                assignedTo = "Neha Gupta",
                priority = TaskPriority.LOW,
                status = TaskStatus.COMPLETED,
                category = TaskCategory.GENERAL,
                dueDate = "05 Oct 2026",
                estimatedHours = 2.0,
                notes = "Distributed 45 employee pay-slips and generated ECR file for EPFO portal."
            )
        )

        private fun initialTodos(): List<TodoItem> = listOf(
            TodoItem("TD-1", "Verify GSTR-2B ITC variance report for Nexus Infotech", isCompleted = true, category = "GST"),
            TodoItem("TD-2", "Obtain physical DSC token from Horizon Logistics director", isCompleted = false, category = "DSC"),
            TodoItem("TD-3", "Upload advance tax payment challan for Q2 assessments", isCompleted = false, category = "Tax"),
            TodoItem("TD-4", "Send monthly retainer invoice to OmniTech Software Labs", isCompleted = true, category = "Billing"),
            TodoItem("TD-5", "Call MCA helpdesk regarding V3 portal DSC signature handshake", isCompleted = false, category = "MCA")
        )

        private fun initialCompliance(): List<ComplianceItem> = listOf(
            ComplianceItem(
                id = "CMP-1",
                formName = "GSTR-3B",
                actName = "CGST / SGST Act 2017",
                period = "September 2026",
                dueDate = "20 Oct 2026",
                category = ComplianceCategory.GST,
                status = FilingStatus.DUE_SOON,
                penaltyAlert = "Late fee ₹50/day applicable after 20th"
            ),
            ComplianceItem(
                id = "CMP-2",
                formName = "GSTR-1",
                actName = "CGST / SGST Act 2017",
                period = "September 2026",
                dueDate = "11 Oct 2026",
                category = ComplianceCategory.GST,
                status = FilingStatus.UPCOMING
            ),
            ComplianceItem(
                id = "CMP-3",
                formName = "TDS Return Form 26Q",
                actName = "Income Tax Act 1961",
                period = "Q2 (Jul - Sep 2026)",
                dueDate = "31 Oct 2026",
                category = ComplianceCategory.TDS,
                status = FilingStatus.UPCOMING
            ),
            ComplianceItem(
                id = "CMP-4",
                formName = "AOC-4 (XBRL / Non-XBRL)",
                actName = "Companies Act 2013",
                period = "FY 2025-26",
                dueDate = "29 Oct 2026",
                category = ComplianceCategory.ROC_MCA,
                status = FilingStatus.UPCOMING,
                penaltyAlert = "Default fine ₹100/day per company officer"
            ),
            ComplianceItem(
                id = "CMP-5",
                formName = "DIR-3 KYC",
                actName = "Companies Act 2013",
                period = "Annual 2026",
                dueDate = "30 Sep 2026",
                category = ComplianceCategory.ROC_MCA,
                status = FilingStatus.FILED,
                acknowledgmentNo = "SRN-R9842109",
                filedDate = "28 Sep 2026"
            ),
            ComplianceItem(
                id = "CMP-6",
                formName = "Advance Tax Installment 2",
                actName = "Income Tax Act 1961",
                period = "FY 2026-27 (45% Cumulative)",
                dueDate = "15 Sep 2026",
                category = ComplianceCategory.INCOME_TAX,
                status = FilingStatus.FILED,
                acknowledgmentNo = "CIN-ITAX-8837190",
                filedDate = "14 Sep 2026"
            )
        )

        private fun initialInvoices(): List<Invoice> = listOf(
            Invoice(
                id = "INV-2026-001",
                invoiceNumber = "APX/26-27/041",
                clientName = "Nexus Infotech Solutions Pvt Ltd",
                issueDate = "01 Oct 2026",
                dueDate = "15 Oct 2026",
                items = listOf(
                    InvoiceItem("Statutory GST Compliance Retainer - Sep 2026", 1, 18000.0),
                    InvoiceItem("TDS Reconciliation & Filing Assistance", 1, 7500.0)
                ),
                taxRatePercent = 18.0,
                subtotal = 25500.0,
                totalAmount = 30090.0,
                paidAmount = 0.0,
                status = InvoiceStatus.UNPAID
            ),
            Invoice(
                id = "INV-2026-002",
                invoiceNumber = "APX/26-27/042",
                clientName = "Horizon Logistics Global",
                issueDate = "25 Sep 2026",
                dueDate = "10 Oct 2026",
                items = listOf(
                    InvoiceItem("Statutory Internal Audit Interim Report", 1, 45000.0),
                    InvoiceItem("Fixed Asset Physical Verification Fee", 1, 15000.0)
                ),
                taxRatePercent = 18.0,
                subtotal = 60000.0,
                totalAmount = 70800.0,
                paidAmount = 35400.0,
                status = InvoiceStatus.PARTIAL
            ),
            Invoice(
                id = "INV-2026-003",
                invoiceNumber = "APX/26-27/043",
                clientName = "Zenith Healthcare & Diagnostics",
                issueDate = "15 Sep 2026",
                dueDate = "30 Sep 2026",
                items = listOf(
                    InvoiceItem("Company Secretarial Annual Maintenance Retainer", 1, 22000.0)
                ),
                taxRatePercent = 18.0,
                subtotal = 22000.0,
                totalAmount = 25960.0,
                paidAmount = 25960.0,
                status = InvoiceStatus.PAID
            )
        )

        private fun initialExpenses(): List<Expense> = listOf(
            Expense(
                id = "EXP-1",
                vendorName = "Cloud Computing & Server Infrastructure",
                category = "IT & Subscriptions",
                date = "01 Oct 2026",
                amount = 14200.0,
                referenceNo = "AWS-99128"
            ),
            Expense(
                id = "EXP-2",
                vendorName = "MCA Portal Government Fees",
                category = "Govt Challan Reimbursement",
                date = "28 Sep 2026",
                amount = 6400.0,
                referenceNo = "MCA-SRN-4412"
            ),
            Expense(
                id = "EXP-3",
                vendorName = "Prime Commercial Office Lease",
                category = "Rent & Utilities",
                date = "01 Oct 2026",
                amount = 75000.0,
                referenceNo = "NEFT-HDFC-9102"
            )
        )

        private fun initialJournalEntries(): List<JournalEntry> = listOf(
            JournalEntry(
                id = "JE-1",
                entryNo = "JV/2026/108",
                date = "01 Oct 2026",
                debitAccount = "Rent Expense A/c",
                creditAccount = "HDFC Bank Operating A/c",
                amount = 75000.0,
                narration = "Being monthly corporate office premises rent paid for October 2026."
            ),
            JournalEntry(
                id = "JE-2",
                entryNo = "JV/2026/109",
                date = "02 Oct 2026",
                debitAccount = "HDFC Bank Operating A/c",
                creditAccount = "Horizon Logistics Global (Debtor)",
                amount = 35400.0,
                narration = "Being 50% part payment received against Invoice APX/26-27/042 via RTGS."
            ),
            JournalEntry(
                id = "JE-3",
                entryNo = "JV/2026/110",
                date = "03 Oct 2026",
                debitAccount = "Staff Salaries & Allowances A/c",
                creditAccount = "Salaries Payable A/c",
                amount = 285000.0,
                narration = "Being payroll accrued for 8 team associates for September 2026."
            )
        )

        private fun initialEmployees(): List<Employee> = listOf(
            Employee("EMP-01", "EMP-1001", "Rohan Sharma", "Senior Tax Manager", "Direct Tax & GST", "rohan.s@taskosphere.com", "+91 98201 12345", 85000.0),
            Employee("EMP-02", "EMP-1002", "Pooja Verma", "Audit Partner", "Statutory Audit", "pooja.v@taskosphere.com", "+91 98202 23456", 110000.0),
            Employee("EMP-03", "EMP-1003", "Amit Patel", "Compliance Associate", "TDS & Payroll", "amit.p@taskosphere.com", "+91 98203 34567", 52000.0),
            Employee("EMP-04", "EMP-1004", "Kavita Rao", "Company Secretary", "Corporate Law", "kavita.r@taskosphere.com", "+91 98204 45678", 78000.0),
            Employee("EMP-05", "EMP-1005", "Adv. Siddharth Joshi", "Legal Counsel", "IP & Trademark", "siddharth.j@taskosphere.com", "+91 98205 56789", 95000.0),
            Employee("EMP-06", "EMP-1006", "Neha Gupta", "HR & Operations Lead", "Administration", "neha.g@taskosphere.com", "+91 98206 67890", 60000.0)
        )

        private fun initialAttendance(): List<AttendanceRecord> = listOf(
            AttendanceRecord("ATT-1", "02 Oct 2026", "09:12 AM", null, 0.0, AttendanceStatus.PRESENT),
            AttendanceRecord("ATT-2", "01 Oct 2026", "09:05 AM", "06:45 PM", 9.6, AttendanceStatus.PRESENT),
            AttendanceRecord("ATT-3", "30 Sep 2026", "09:20 AM", "06:30 PM", 9.1, AttendanceStatus.PRESENT),
            AttendanceRecord("ATT-4", "29 Sep 2026", "09:15 AM", "06:10 PM", 8.9, AttendanceStatus.PRESENT),
            AttendanceRecord("ATT-5", "28 Sep 2026", "09:30 AM", "01:30 PM", 4.0, AttendanceStatus.HALF_DAY)
        )

        private fun initialLeaves(): List<LeaveApplication> = listOf(
            LeaveApplication("LV-1", "Rohan Sharma", "Casual Leave", "12 Oct 2026", "13 Oct 2026", 2, "Family festive celebration", "Approved"),
            LeaveApplication("LV-2", "Amit Patel", "Sick Leave", "05 Oct 2026", "06 Oct 2026", 2, "Seasonal viral recovery", "Approved"),
            LeaveApplication("LV-3", "Neha Gupta", "Earned Leave", "24 Oct 2026", "28 Oct 2026", 5, "Diwali vacation trip", "Pending")
        )

        private fun initialClients(): List<Client> = listOf(
            Client("CL-01", "Nexus Infotech Solutions Pvt Ltd", "AAACN1234F", "27AAACN1234F1Z8", "Rajesh Singhania", "accounts@nexusinfotech.io", "+91 98110 99887", "Mumbai", listOf("GST", "Income Tax", "TDS")),
            Client("CL-02", "Horizon Logistics Global", "AABCH5678K", "29AABCH5678K1ZD", "Vikas Malhotra", "cfo@horizonlogistics.com", "+91 98220 88776", "Bengaluru", listOf("Statutory Audit", "Taxation")),
            Client("CL-03", "Zenith Healthcare & Diagnostics", "AABCZ9012L", "07AABCZ9012L1Z2", "Dr. Meera Nambiar", "director@zenithhealth.org", "+91 98330 77665", "New Delhi", listOf("ROC / MCA", "GST")),
            Client("CL-04", "OmniTech Software Labs", "AAECO3456P", "24AAECO3456P1ZB", "Karan Dave", "contact@omnitechlabs.com", "+91 98440 66554", "Ahmedabad", listOf("IP / Trademark", "Corporate Law"))
        )

        private fun initialDscRecords(): List<DscRecord> = listOf(
            DscRecord("DSC-01", "Rajesh Singhania", "Nexus Infotech Solutions", "Class 3 Signing & Encryption", "EPASS2003-88491", "14 Nov 2026", "Locker A - Drawer 2", 43),
            DscRecord("DSC-02", "Vikas Malhotra", "Horizon Logistics Global", "Class 3 Signing Only", "WATCHDATA-99214", "08 Oct 2026", "Locker A - Drawer 1", 6),
            DscRecord("DSC-03", "Dr. Meera Nambiar", "Zenith Healthcare", "Class 3 Combo Token", "HYP2003-12830", "18 Feb 2027", "Locker B - Token Box 4", 139),
            DscRecord("DSC-04", "Karan Dave", "OmniTech Software Labs", "Class 3 Signing & Encryption", "EPASS2003-66219", "02 Dec 2026", "Locker B - Token Box 1", 61)
        )

        private fun initialCredentials(): List<PassvaultCredential> = listOf(
            PassvaultCredential("PV-01", "Nexus Infotech Solutions", "GST Portal (gst.gov.in)", "https://services.gst.gov.in", "nexus_gst_admin", notes = "Primary GST return filing credentials"),
            PassvaultCredential("PV-02", "Nexus Infotech Solutions", "Income Tax e-Filing", "https://eportal.incometax.gov.in", "AAACN1234F", notes = "Corporate PAN e-filing login"),
            PassvaultCredential("PV-03", "Horizon Logistics Global", "MCA V3 Corporate Filing", "https://mca.gov.in/v3", "horizon_mca_biz", notes = "V3 Business user linked with director DIN"),
            PassvaultCredential("PV-04", "Zenith Healthcare", "TRACES TDS Portal", "https://contents.tdscpc.gov.in", "ZENITH_TDS", notes = "Quarterly Form 16/16A generation login")
        )

        private fun initialTrademarks(): List<TrademarkRecord> = listOf(
            TrademarkRecord("TM-01", "5482910", "OMNITECH", 42, "OmniTech Software Labs", "12 Mar 2025", "Registered", "Computer programming, SaaS platforms and cloud software development."),
            TrademarkRecord("TM-02", "5912440", "ZENITHCARE", 44, "Zenith Healthcare & Diagnostics", "20 Aug 2025", "Objected", "Medical services, clinical diagnostics, pathology laboratories and healthcare clinics."),
            TrademarkRecord("TM-03", "6109281", "HORIZON FREIGHT", 39, "Horizon Logistics Global", "05 Jan 2026", "Formalities Chk Pass", "Packaging and storage of goods, freight transport, supply chain logistics."),
            TrademarkRecord("TM-04", "6320194", "NEXUSFLOW", 9, "Nexus Infotech Solutions", "14 Jul 2026", "Examined", "Downloadable software applications, mobile apps, database management software.")
        )
    }
}
