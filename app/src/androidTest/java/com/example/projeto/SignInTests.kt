package com.example.projeto

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.projeto.ui.MainActivity
import org.junit.Rule
import org.junit.Test

class SignInTests {

    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private val username = "manuela"
    private val email = "manuela@gmail.com"
    private val password = "senha123"

    @Test
    fun shouldSignUpWithAllNeededData() {
        clickOnButton(R.id.link_login_activity)
        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.user_email_cadastro_activity, email)
        fillBlanks(R.id.password_cadastro_activity, password)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        onView(withText("Cadastro realizado com sucesso!"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))

        fillBlanks(R.id.user_email_login_activity, email)
        fillBlanks(R.id.password_login_activity, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText("Login efetuado"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun shouldToastMessageWhenTryingToSignInAUserThatDoesNotExist() {
        fillBlanks(R.id.user_email_login_activity, email)
        fillBlanks(R.id.password_login_activity, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText("Usuário não encontrado. Faça o cadastro"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUserWithoutEmail() {
        fillBlanks(R.id.password_login_activity, password)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText("Preencha todos os campos"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUserWithoutPassword() {
        fillBlanks(R.id.user_email_login_activity, email)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText("Preencha todos os campos"))
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