package com.example.projeto.signInTests

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
import com.example.projeto.signUpTests.SignUpTestsLowerApi
import com.example.projeto.ui.user.LoginActivity
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch

class SignInTestsLowerApi {

    @get:Rule
    val rule = ActivityScenarioRule(LoginActivity::class.java)

    private val emailTest1 = "teste@gmail.com"
    private val emailTest2 = "testelogin@gmail.com"
    private val password = "senha123"
    private val countDownLatch = CountDownLatch(1)

    @Test
    fun shouldToastMessageWhenTryingToSignInAUserThatDoesNotExist() {
        fillBlanksSignInActivity(emailTest2, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText(R.string.user_not_found))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldSignUpWithAllNeededData() {
        clickOnButton(R.id.link_login_activity)
        SignUpTestsLowerApi().fillBlanksSignUpActivity(emailTest1)
        clickOnButton(R.id.btn_enter_cadastro_activity)

        countDownLatch.countDown()
        fillBlanksSignInActivity(emailTest1, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText(R.string.logged_in))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUserWithoutEmail() {
        fillBlanks(R.id.password_login_activity, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText(R.string.preencha_todos_os_campos))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUserWithoutPassword() {
        fillBlanks(R.id.user_email_login_activity, emailTest1)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText(R.string.preencha_todos_os_campos))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUserWithWrongPassword() {
        clickOnButton(R.id.link_login_activity)

        SignUpTestsLowerApi().fillBlanksSignUpActivity(emailTest2)
        clickOnButton(R.id.btn_enter_cadastro_activity)

        fillBlanksSignInActivity(emailTest2, "senha errada")
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText(R.string.senha_incorreta))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    private fun fillBlanksSignInActivity(email: String, password: String) {
        fillBlanks(R.id.user_email_login_activity, email)
        fillBlanks(R.id.password_login_activity, password)
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