package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.IDefaultTasksRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest {

    private lateinit var tasksRepository: IDefaultTasksRepository

    @Before
    fun setup() {
        tasksRepository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = tasksRepository
    }

    @After
    fun teardown() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun activeTask_displayedInUI() = runBlockingTest {
        //Given
        val task = Task("Active Task", "Checking is displayed", false)
        tasksRepository.saveTask(task)
        //When
        val bundle = TaskDetailFragmentArgs(task.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        Espresso.onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.task_detail_title_text)).check(matches(withText("Active Task")))
        Espresso.onView(withId(R.id.task_detail_description_text)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.task_detail_description_text)).check(matches(withText("Checking is displayed")))
        Espresso.onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))
        Espresso.onView(withId(R.id.task_detail_complete_checkbox)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.task_detail_complete_checkbox)).check(matches(isChecked()))
    }

}