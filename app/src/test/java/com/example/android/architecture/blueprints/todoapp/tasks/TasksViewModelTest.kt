package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.*
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.IDefaultTasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewmodel: TasksViewModel
    lateinit var tasksRepository  : FakeTestRepository

    @Before
    fun setup(){
        viewmodel = TasksViewModel((ApplicationProvider.getApplicationContext() as TodoApplication).tasksRepository)
        tasksRepository = FakeTestRepository()
        ServiceLocator.tasksRepository = tasksRepository
    }

    @After
    fun tearDownDispatcher(){
        Dispatchers.resetMain()
    }

    @Test
    fun addNewTask_setsNewTaskEvent(){
        //Create an observer for viewmodel
        val observer = Observer<Event<Unit>>{}
        try{
            viewmodel.newTaskEvent.observeForever(observer)

            viewmodel.addNewTask()
            val value = viewmodel.newTaskEvent.getOrAwaitValue()
            assertThat(value.getContentIfNotHandled(), (not(CoreMatchers.nullValue())))
        } finally {
            viewmodel.newTaskEvent.removeObserver(observer)
        }

        // Then the new task event is triggered
    }

    @Test
    fun setFilterTask_tasksAddViewVisible() {
        viewmodel.setFiltering(TasksFilterType.ALL_TASKS)

        val value = viewmodel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is`(true))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // Create an active task and add it to the repository.
        val task = Task("Title", "Description", true)
        tasksRepository.addTasks(task)

        // Mark the task as complete task.
        viewmodel.completeTask(task, true)

        // Verify the task is completed.
        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Event<Int> =  viewmodel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}