package com.example.xpen.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val dao: ExpenseDao) {

    fun getExpensesBetween(start: Long, end: Long): Flow<List<Expense>> =
        dao.getExpensesBetween(start, end)

    suspend fun insert(expense: Expense) = dao.insert(expense)

    suspend fun update(expense: Expense) = dao.update(expense)

    suspend fun delete(expense: Expense) = dao.delete(expense)

    suspend fun deleteOlderThan(cutoff: Long) = dao.deleteOlderThan(cutoff)
}