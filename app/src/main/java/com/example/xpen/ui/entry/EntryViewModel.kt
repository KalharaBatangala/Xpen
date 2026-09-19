package com.example.xpen.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpen.data.Expense
import com.example.xpen.data.ExpenseRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
class EntryViewModel(private val repository: ExpenseRepository) : ViewModel() {

    fun saveExpense(amount: Double, category: String, onSaved: () -> Unit) {
        viewModelScope.launch {
            repository.insert(
                Expense(
                    amount = amount,
                    category = category,
                    dateTime = System.currentTimeMillis()
                )
            )
            onSaved()
        }
    }
}



class EntryViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EntryViewModel(repository) as T
    }
}