package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewmodel: TasksViewModel

    @Before
    fun setup(){
        viewmodel = TasksViewModel(ApplicationProvider.getApplicationContext())
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
}