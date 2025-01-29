package com.example.projeto.searchTests

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.projeto.R
import com.example.projeto.ToastMatcher
import com.example.projeto.expresions.UserPreferences
import com.example.projeto.ui.user.MainActivity
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SearchTestsLowerApi {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private val pesquisa = "Box Peter Pan"
    private val countDownLatch = CountDownLatch(5)

    @Before
    fun mockUserPreferences() {
        val context = mockk<Context>()
        val preferences = UserPreferences(context)

        runBlocking {
            preferences.clearUserStatus()
            preferences.saveUserStatus(true)
        }

        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun shouldSearchABookWhenTheSearchButtonIsClicked() {
        fillBlanks(R.id.edit_text_main_activity, pesquisa)

        countDownLatch.countDown()
        clickOnButton(R.id.btn_search_main_activity)

        countDownLatch.await(3, TimeUnit.SECONDS)
        onView(withId(R.id.recycler_search_activity)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun shouldShowToastMessageWhenClickingOnSearchButtonBeforeTypingAText() {
        fillBlanks(R.id.edit_text_main_activity, "")

        clickOnButton(R.id.btn_search_main_activity)

        countDownLatch.countDown()
        onView(withText("Pesquise um título ou autor"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowToastMessageWhenTryingToSearchABookThatDoesNotExist() {
        fillBlanks(R.id.edit_text_main_activity, "lksjdfaljklasf")

        clickOnButton(R.id.btn_search_main_activity)

        countDownLatch.countDown()
        onView(withText("Erro ao pesquisar"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    private fun clickOnButton(button: Int) {
        onView(withId(button)).perform(
            click()
        )
    }

    private fun fillBlanks(layout: Int, text: String) {
        onView(withId(layout)).perform(
            typeText(text),
            closeSoftKeyboard()
        )
    }
}