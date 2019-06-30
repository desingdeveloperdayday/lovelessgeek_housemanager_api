package com.lovelessgeek.housemanager.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class TaskDao : BaseDao<TaskEntity> {

    @Delete
    abstract suspend fun deleteTask(task: TaskEntity)

    @Transaction
    open suspend fun deleteTasks(tasks: List<TaskEntity>) {
        tasks.forEach { task -> deleteTask(task) }
    }

    @Query("SELECT * FROM task")
    abstract suspend fun loadAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE isComplete = 0")
    abstract suspend fun loadTodoTasks(): List<TaskEntity>

    @Query("SELECT * FROM task WHERE isComplete = 1")
    abstract suspend fun loadCompletedTasks(): List<TaskEntity>

    //@Query("SELECT * FROM task WHERE time > :time")
    //abstract fun loadTasksAfter(time: Date): List<TaskEntity>

    @Query("DELETE from task")
    abstract suspend fun deleteAll()
}