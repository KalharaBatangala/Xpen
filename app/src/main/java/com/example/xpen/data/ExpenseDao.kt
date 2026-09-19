package com.example.xpen.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses WHERE dateTime BETWEEN :start AND :end ORDER BY dateTime DESC")
    fun getExpensesBetween(start: Long, end: Long): Flow<List<Expense>>

    @Query("DELETE FROM expenses WHERE dateTime < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)
}