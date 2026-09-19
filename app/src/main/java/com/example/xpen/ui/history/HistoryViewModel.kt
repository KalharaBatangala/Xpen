package com.example.xpen.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.xpen.data.Expense
import com.example.xpen.data.ExpenseRepository
import com.example.xpen.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: ExpenseRepository,
    route: String
) : ViewModel() {

    private val range = DateUtils.rangeForScreen(route)
    private val start = range.first
    private val end = range.second
    val expenses: StateFlow<List<Expense>> = repository.getExpensesBetween(start, end)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.delete(expense) }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch { repository.update(expense) }
    }
}

class HistoryViewModelFactory(
    private val repository: ExpenseRepository,
    private val route: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HistoryViewModel(repository, route) as T
    }
}