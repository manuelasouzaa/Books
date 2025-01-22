package com.example.projeto

import androidx.test.InstrumentationRegistry
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.viewpager.widget.ViewPager.DecorView
import com.example.projeto.ui.CadastroActivity
import com.example.projeto.ui.MainActivity
import com.google.android.material.internal.ContextUtils.getActivity
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test

class UserTests {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private val username = "manuela"
    private val email = "manuela@gmail.com"
    private val senha = "senha123"
    private val pesquisa = "Box Peter Pan"

    @Test
    fun happyEndingJourney() {
        signUpWithAllNeededData()
        loginUser()
        accountActivityShowsUsernameAndEmail()

        clickOnButton(R.id.btn_return_account_activity)

        searchABook()
        openBookDetails()
        addBookToBooklist()
    }

    @Test
    fun accountActivityShowsUsernameAndEmail() {
        clickOnButton(R.id.btn_account_main_activity)

        onView(withId(R.id.user_email_account_activity)).check(
            matches(withText(email))
        )
        onView(withId(R.id.username_account_activity)).check(
            matches(withText(username))
        )
    }

    @Test
    fun signUpWithAllNeededData() {
        clickOnButton(R.id.link_login_activity)

        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.user_email_cadastro_activity, email)
        fillBlanks(R.id.password_cadastro_activity, senha)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        onView(withText("Login")).check(
            matches(isDisplayed())
        )

        Thread.sleep(2000)
    }

    @Test
    fun loginUser() {
        fillBlanks(R.id.user_email_login_activity, email)
        fillBlanks(R.id.password_login_activity, senha)
        clickOnButton(R.id.btn_enter_login_activity)

        Thread.sleep(1000)

        onView(withId(R.id.title_main_activity)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun searchABook() {
        Thread.sleep(2000)
        fillBlanks(R.id.edit_text_main_activity, pesquisa)
        clickOnButton(R.id.btn_search_main_activity)

        Thread.sleep(2000)

        onView(withText(pesquisa)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun openBookDetails() {
        onView(withText(pesquisa)).perform(
            click()
        )

        Thread.sleep(2000)

        onView(withId(R.id.book_title_book_details_activity)).check(
            matches(withText(pesquisa))
        )
    }

    @Test
    fun addBookToBooklist() {
        clickOnButton(R.id.btn_add_book_details)

        Thread.sleep(1000)

        onView(withId(R.id.btn_close_saved_book_dialog)).check(
            matches(isDisplayed())
        )
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