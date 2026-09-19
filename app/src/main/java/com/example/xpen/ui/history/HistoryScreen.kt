package com.example.xpen.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xpen.XpenApplication
import com.example.xpen.data.Expense
import com.example.xpen.util.CurrencyUtils
import com.example.xpen.util.DateUtils
import com.example.xpen.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(screen: Screen) {
    val context = LocalContext.current
    val app = context.applicationContext as XpenApplication
    val viewModel: HistoryViewModel = viewModel(
        key = screen.route,
        factory = HistoryViewModelFactory(app.repository, screen.route)
    )

    val expenses by viewModel.expenses.collectAsState()

    // Only Today and This Week ever allow edit/delete
    val screenAllowsEditing = screen.route == Screen.Today.route || screen.route == Screen.ThisWeek.route

    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }
    var expenseToDelete by remember { mutableStateOf<Expense?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Date & Time", modifier = Modifier.weight(2f), style = MaterialTheme.typography.labelLarge)
            Text("Amount", modifier = Modifier.weight(1.3f), style = MaterialTheme.typography.labelLarge)
            Text("Category", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        if (expenses.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text("No expenses recorded.")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(expenses, key = { it.id }) { expense ->
                    val canEditThis = screenAllowsEditing && DateUtils.isWithinCorrectionWindow(expense.dateTime)

                    if (canEditThis) {
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { value ->
                                when (value) {
                                    SwipeToDismissBoxValue.EndToStart -> { expenseToDelete = expense; false }
                                    SwipeToDismissBoxValue.StartToEnd -> { expenseToEdit = expense; false }
                                    else -> false
                                }
                            }
                        )
                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val (color, icon, alignment) = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.StartToEnd -> Triple(Color(0xFF2196F3), Icons.Filled.Edit, Alignment.CenterStart)
                                    SwipeToDismissBoxValue.EndToStart -> Triple(Color(0xFFE53935), Icons.Filled.Delete, Alignment.CenterEnd)
                                    else -> Triple(Color.Transparent, null, Alignment.Center)
                                }
                                Box(
                                    modifier = Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp),
                                    contentAlignment = alignment
                                ) {
                                    icon?.let { Icon(it, contentDescription = null, tint = Color.White) }
                                }
                            }
                        ) {
                            ExpenseRow(expense)
                        }
                    } else {
                        ExpenseRow(expense)
                    }
                    HorizontalDivider()
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Export as:", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = {
                val file = com.example.xpen.util.ExportUtils.exportToCsv(context, expenses, screen.route)
                com.example.xpen.util.ExportUtils.shareFile(context, file, "text/csv")
            }) { Text("CSV") }
            OutlinedButton(onClick = {
                val file = com.example.xpen.util.ExportUtils.exportToPdf(context, expenses, screen.route, screen.title)
                com.example.xpen.util.ExportUtils.shareFile(context, file, "application/pdf")
            }) { Text("PDF") }
        }
    }

    expenseToEdit?.let { expense ->
        EditExpenseDialog(
            expense = expense,
            onDismiss = { expenseToEdit = null },
            onConfirm = { updated ->
                viewModel.updateExpense(updated)
                expenseToEdit = null
            }
        )
    }

    expenseToDelete?.let { expense ->
        DeleteConfirmDialog(
            onDismiss = { expenseToDelete = null },
            onConfirm = {
                viewModel.deleteExpense(expense)
                expenseToDelete = null
            }
        )
    }
}

@Composable
private fun ExpenseRow(expense: Expense) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(DateUtils.formatDateTime(expense.dateTime), modifier = Modifier.weight(2f))
        Text(CurrencyUtils.format(expense.amount), modifier = Modifier.weight(1.3f))
        Text(expense.category, modifier = Modifier.weight(1f))
    }
}