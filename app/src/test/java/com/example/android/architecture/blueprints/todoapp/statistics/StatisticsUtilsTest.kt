package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest{

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero(){
        val tasks = listOf<Task>(Task("title", "desc", isCompleted = false))
        //Create active task

        //Call the function
        val result = getActiveAndCompletedStats(tasks)

        //Check the result
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero(){
        val tasks = emptyList<Task>()
        //Create active task

        //Call the function
        val result = getActiveAndCompletedStats(tasks)

        //Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompleteStats_noActive_returnsZeroHundred(){
        val tasks = listOf<Task>(Task("title", "desc", isCompleted = true))
        //Create active task

        //Call the function
        val result = getActiveAndCompletedStats(tasks)

        //Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompleteStats_null_returnsZero(){
        //Call the function
        val result = getActiveAndCompletedStats(null)

        //Check the result
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompleteStats_both_returnsFortySix(){
        val tasks = listOf<Task>(
                Task("title", "desc", isCompleted = true),
                Task("title2", "desc2", isCompleted = true),
                Task("title", "desc", isCompleted = true),
                Task("title", "desc", isCompleted = false),
                Task("title", "desc", isCompleted = false))
        //Create active task

        //Call the function
        val result = getActiveAndCompletedStats(tasks)

        //Check the result
        assertThat(result.activeTasksPercent, `is`(40f))
        assertThat(result.completedTasksPercent, `is`(60f))
    }
}