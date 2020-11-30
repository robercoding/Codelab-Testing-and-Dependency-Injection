package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {

    @get: Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var viewmodel: StatisticsViewModel
    lateinit var fakeTestRepository: FakeTestRepository

    @Before
    fun setup() {
        fakeTestRepository = FakeTestRepository()
        viewmodel = StatisticsViewModel(fakeTestRepository)
    }


    @Test
    fun loadTasks_loading() {
        mainCoroutineRule.pauseDispatcher()

        viewmodel.refresh()

        assertThat(viewmodel.dataLoading.getOrAwaitValue(), `is`(true))

        mainCoroutineRule.resumeDispatcher()

        assertThat(viewmodel.dataLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun getTask_returnError() = runBlockingTest {
        fakeTestRepository.setReturnError(true)
        viewmodel.refresh()

        assertThat(viewmodel.error.getOrAwaitValue(), `is`(true))
        assertThat(viewmodel.empty.getOrAwaitValue(), `is`(true))
    }
}