package com.example.xpen

import android.app.Application
import com.example.xpen.data.ExpenseDatabase
import com.example.xpen.data.ExpenseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Calendar

class XpenApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database by lazy { ExpenseDatabase.getInstance(this) }
    val repository by lazy { ExpenseRepository(database.expenseDao()) }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            val cutoff = Calendar.getInstance().apply { add(Calendar.MONTH, -3) }.timeInMillis
            repository.deleteOlderThan(cutoff)
        }
    }
}