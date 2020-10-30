package com.example.taskstodo.domain

import androidx.lifecycle.LiveData
import com.example.taskstodo.data.Task
import com.example.taskstodo.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {

    val allTasks: LiveData<List<Task>> = taskDao.getAscTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

     fun getTaskById(id : Long) : LiveData<Task> {
         return taskDao.getTaskWithId(id)
    }
}