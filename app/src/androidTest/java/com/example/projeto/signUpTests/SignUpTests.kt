package com.example.projeto.signUpTests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.projeto.R
import com.example.projeto.ui.user.CadastroActivity
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class SignUpTests {

    @get:Rule
    val rule = ActivityScenarioRule(CadastroActivity::class.java)

    private val username = "teste"
    private val emailTest1 = "teste@gmail.com"
    private val emailTest2 = "testecadastro@gmail.com"
    private val password = "senha123"
    private val countDownLatch = CountDownLatch(1)

    @Test
    fun shouldSignUpWithAllNeededData() {
        fillBlanksSignUpActivity(emailTest1)
        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        onView(withId(R.id.titulo_login_activity))
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldShowErrorMessageWhenTryingToSignUpRegisteredEmail() {
        fillBlanksSignUpActivity(emailTest2)
        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        clickOnButton(R.id.link_login_activity)

        fillBlanksSignUpActivity(emailTest2)
        clickOnButton(R.id.btn_enter_cadastro_activity)

        onView(withText("Login"))
            .check(doesNotExist())
    }

    @Test
    fun shouldShowErrorMessageWhenTryingToSignUpWithoutEmail() {
        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.password_cadastro_activity, password)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        onView(withText("Login"))
            .check(doesNotExist())
    }

    @Test
    fun shouldShowErrorMessageWhenTryingToSignUpWithoutUsername() {
        fillBlanks(R.id.user_email_cadastro_activity, emailTest1)
        fillBlanks(R.id.password_cadastro_activity, password)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        onView(withText("Login"))
            .check(doesNotExist())
    }

    @Test
    fun shouldShowErrorMessageWhenTryingToSignUpWithoutPassword() {
        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.user_email_cadastro_activity, emailTest1)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        onView(withText("Login"))
            .check(doesNotExist())
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

    fun fillBlanksSignUpActivity(email: String) {
        fillBlanks(R.id.user_email_cadastro_activity, email)
        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.password_cadastro_activity, password)
    }
}