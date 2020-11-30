package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksLocalDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var tasksLocalDataSource : TasksDataSource
    lateinit var database : ToDoDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ToDoDatabase::class.java).allowMainThreadQueries().build()
        tasksLocalDataSource = TasksLocalDataSource(database.taskDao(), Dispatchers.Main)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun saveTask_retrievesTask() = runBlocking {
        val task = Task("Title", "Description")
        tasksLocalDataSource.saveTask(task)
        val result = tasksLocalDataSource.getTask(task.id)

        assertThat(result.succeeded, `is`(true))
        result as Result.Success

        assertThat(result.data, `is`(task))
        assertThat(result.data.id, `is`(task.id))
        assertThat(result.data.title, `is`(task.title))
        assertThat(result.data.description, `is`(task.description))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking{
        // 1. Given
        val newTask = Task("Task completed", "Task completed", false)
        tasksLocalDataSource.saveTask(newTask)

        // 2. When
        tasksLocalDataSource.completeTask(newTask)

        // 3. Then
        val resultCompletedTask = tasksLocalDataSource.getTask(newTask.id)

        assertThat(resultCompletedTask.succeeded, `is`(true))
        resultCompletedTask as Result.Success

        val completedTask = resultCompletedTask.data
        assertThat(completedTask.title, containsString(newTask.title))
        assertThat(completedTask.isCompleted, `is`(true))
    }
}