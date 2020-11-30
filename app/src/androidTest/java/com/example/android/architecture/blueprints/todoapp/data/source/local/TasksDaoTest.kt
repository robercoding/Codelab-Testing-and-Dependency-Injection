package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksDaoTest {
    @get:Rule
    val instantTaskExecutoRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun initDB() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                ToDoDatabase::class.java
        ).build()
    }

    @After
    fun closeDB(){
        database.close()
    }

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        // GIVEN - Insert a task.
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        // WHEN - Get the task by id from the database.
        val loaded = database.taskDao().getTaskById(task.id)

        // THEN - The loaded data contains the expected values.
        assertThat<Task>(loaded as Task, notNullValue(Task::class.java))
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun insertTaskAndUpdateTask() = runBlockingTest{
        val task = Task("title", "description", false)
        database.taskDao().insertTask(task)

        val loaded = database.taskDao().getTaskById(task.id)

        assertThat(loaded as Task, notNullValue(Task::class.java))

        task.title = "updated title"
        task.description = "updated description"
        database.taskDao().updateTask(task)

        val updatedLoaded = database.taskDao().getTaskById(task.id)

        assertThat(updatedLoaded as Task,  notNullValue(Task::class.java))
        assertThat(updatedLoaded.title,  containsString(task.title))
        assertThat(updatedLoaded.description,  containsString(task.description))
        assertThat(updatedLoaded.isCompleted,  `is`(false))
    }
}