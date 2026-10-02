package com.example.taskosphere.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskosphere.model.*
import com.example.taskosphere.ui.TaskosphereViewModel
import com.example.taskosphere.ui.theme.*

@Composable
fun TasksScreen(
    viewModel: TaskosphereViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.filteredTasks.collectAsState()
    val allTasks by viewModel.tasks.collectAsState()
    val todos by viewModel.todos.collectAsState()
    val selectedStatus by viewModel.taskStatusFilter.collectAsState()
    val selectedCategory by viewModel.taskCategoryFilter.collectAsState()

    var showTodosSheet by remember { mutableStateOf(false) }
    var newTodoText by remember { mutableStateOf("") }

    val pendingCount = allTasks.count { it.status == TaskStatus.PENDING }
    val inProgressCount = allTasks.count { it.status == TaskStatus.IN_PROGRESS }
    val urgentCount = allTasks.count { it.priority == TaskPriority.URGENT || it.priority == TaskPriority.HIGH }
    val completedCount = allTasks.count { it.status == TaskStatus.COMPLETED }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddTaskDialog.value = true },
                containerColor = BrandNavy,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
                text = { Text("New Task", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("fab_add_task")
            )
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
                Spacer(modifier = Modifier.height(6.dp))
                // Quick Statistics Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "In Progress",
                        value = inProgressCount.toString(),
                        subtitle = "Active tasks",
                        accentColor = BrandBlue,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Pending",
                        value = pendingCount.toString(),
                        subtitle = "To begin",
                        accentColor = StatusWarning,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "High Priority",
                        value = urgentCount.toString(),
                        subtitle = "Urgent / High",
                        accentColor = StatusDanger,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Todo Checklist Banner
            item {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Checklist,
                                    contentDescription = null,
                                    tint = BrandEmerald,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Quick Checklist (${todos.count { it.isCompleted }}/${todos.size})",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandNavy
                                )
                            }
                            TextButton(onClick = { showTodosSheet = !showTodosSheet }) {
                                Text(if (showTodosSheet) "Hide" else "Manage Todos", fontSize = 12.sp)
                            }
                        }

                        AnimatedVisibility(visible = showTodosSheet) {
                            Column(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                todos.forEach { todo ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.toggleTodo(todo.id) }
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Checkbox(
                                            checked = todo.isCompleted,
                                            onCheckedChange = { viewModel.toggleTodo(todo.id) },
                                            colors = CheckboxDefaults.colors(checkedColor = BrandEmerald)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = todo.title,
                                            fontSize = 13.sp,
                                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                            color = if (todo.isCompleted) BrandMutedText else BrandDarkText,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = { viewModel.deleteTodo(todo.id) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = newTodoText,
                                        onValueChange = { newTodoText = it },
                                        placeholder = { Text("Add rapid reminder item...", fontSize = 12.sp) },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f).height(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = {
                                            if (newTodoText.isNotBlank()) {
                                                viewModel.addTodo(newTodoText, "General")
                                                newTodoText = ""
                                            }
                                        },
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(BrandNavy)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "TASK DIRECTORY & WORKFLOW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandMutedText,
                        letterSpacing = 0.8.sp
                    )

                    // Status Filters
                    val statusScroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(statusScroll),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedStatus == null,
                            onClick = { viewModel.setTaskStatusFilter(null) },
                            label = { Text("All Status (${allTasks.size})", fontSize = 11.sp) }
                        )
                        TaskStatus.values().forEach { st ->
                            val count = allTasks.count { it.status == st }
                            FilterChip(
                                selected = selectedStatus == st,
                                onClick = { viewModel.setTaskStatusFilter(st) },
                                label = { Text("${st.label} ($count)", fontSize = 11.sp) }
                            )
                        }
                    }

                    // Category Filters
                    val catScroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(catScroll),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.setTaskCategoryFilter(null) },
                            label = { Text("All Categories", fontSize = 11.sp) }
                        )
                        TaskCategory.values().forEach { cat ->
                            FilterChip(
                                selected = selectedCategory == cat,
                                onClick = { viewModel.setTaskCategoryFilter(cat) },
                                label = { Text(cat.label, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            }

            // Tasks List
            if (tasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = null,
                                tint = BrandEmerald,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No tasks found in this view",
                                fontWeight = FontWeight.SemiBold,
                                color = BrandNavy
                            )
                            Text(
                                text = "Clear filters or click 'New Task' to assign workflow items.",
                                fontSize = 12.sp,
                                color = BrandMutedText
                            )
                        }
                    }
                }
            } else {
                items(tasks, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onStatusChange = { newStatus -> viewModel.updateTaskStatus(task.id, newStatus) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onStatusChange: (TaskStatus) -> Unit,
    onDelete: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("task_item_${task.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Category & Priority
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = BrandBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = task.category.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandBlue,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (prioBg, prioFg) = when (task.priority) {
                        TaskPriority.URGENT -> StatusDanger.copy(alpha = 0.15f) to StatusDanger
                        TaskPriority.HIGH -> StatusWarning.copy(alpha = 0.15f) to StatusWarning
                        TaskPriority.MEDIUM -> StatusInfo.copy(alpha = 0.15f) to StatusInfo
                        TaskPriority.LOW -> Color.Gray.copy(alpha = 0.15f) to Color.Gray
                    }
                    Surface(color = prioBg, shape = RoundedCornerShape(6.dp)) {
                        Text(
                            text = task.priority.label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = prioFg,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { expandedMenu = true }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = BrandMutedText)
                        }
                        DropdownMenu(expanded = expandedMenu, onDismissRequest = { expandedMenu = false }) {
                            TaskStatus.values().forEach { st ->
                                DropdownMenuItem(
                                    text = { Text("Move to ${st.label}") },
                                    onClick = {
                                        onStatusChange(st)
                                        expandedMenu = false
                                    }
                                )
                            }
                            Divider()
                            DropdownMenuItem(
                                text = { Text("Delete Task", color = StatusDanger) },
                                onClick = {
                                    onDelete()
                                    expandedMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BrandNavy
            )

            // Client Name
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Default.Business, contentDescription = null, tint = BrandMutedText, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = task.clientName,
                    fontSize = 12.sp,
                    color = BrandDarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            if (task.notes.isNotBlank()) {
                Text(
                    text = task.notes,
                    fontSize = 12.sp,
                    color = BrandMutedText,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = BrandBorder)
            Spacer(modifier = Modifier.height(8.dp))

            // Footer: Assignee & Due Date & Status Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(BrandNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = task.assignedTo.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = task.assignedTo,
                        fontSize = 12.sp,
                        color = BrandDarkText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = BrandMutedText, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = task.dueDate,
                        fontSize = 11.sp,
                        color = BrandMutedText
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    val (statusColor, statusBg) = when (task.status) {
                        TaskStatus.COMPLETED -> BrandEmerald to BrandEmerald.copy(alpha = 0.15f)
                        TaskStatus.IN_PROGRESS -> BrandBlue to BrandBlue.copy(alpha = 0.15f)
                        TaskStatus.REVIEW -> StatusPurple to StatusPurple.copy(alpha = 0.15f)
                        TaskStatus.PENDING -> StatusWarning to StatusWarning.copy(alpha = 0.15f)
                    }

                    Surface(
                        color = statusBg,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable {
                            val next = when (task.status) {
                                TaskStatus.PENDING -> TaskStatus.IN_PROGRESS
                                TaskStatus.IN_PROGRESS -> TaskStatus.REVIEW
                                TaskStatus.REVIEW -> TaskStatus.COMPLETED
                                TaskStatus.COMPLETED -> TaskStatus.PENDING
                            }
                            onStatusChange(next)
                        }
                    ) {
                        Text(
                            text = task.status.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 11.sp, color = BrandMutedText, fontWeight = FontWeight.Medium)
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            Text(text = subtitle, fontSize = 10.sp, color = BrandMutedText)
        }
    }
}
