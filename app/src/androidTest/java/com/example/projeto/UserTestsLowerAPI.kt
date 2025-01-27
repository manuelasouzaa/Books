package com.example.projeto

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.projeto.contextExpresions.UserPreferences
import com.example.projeto.model.User
import com.example.projeto.ui.MainActivity
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class UserTestsLowerAPI {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    private val username = "manuela"
    private val email = "manuela@gmail.com"
    private val senha = "senha123"
    private val pesquisa = "Box Peter Pan"

    @Test
    fun testingAllTests() {
        signUpWithoutWritingPassword()

        onView(withId(R.id.username_cadastro_activity)).perform(clearText())
        onView(withId(R.id.user_email_cadastro_activity)).perform(clearText())

        signUpWithAllNeededData()
        loginUser()
//        accountActivityShowsUsernameAndEmail()
        searchABook()
        addBookToBooklist()
        goToBooklistThroughDialog()
        removeSavedBook()
    }

    @Test
    fun signUpWithoutWritingPassword() {
        clickOnButton(R.id.link_login_activity)

        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.user_email_cadastro_activity, email)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        onView(withText("Preencha todos os campos"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun signUpWithAllNeededData() {
//        clickOnButton(R.id.link_login_activity)
        fillBlanks(R.id.username_cadastro_activity, username)
        fillBlanks(R.id.user_email_cadastro_activity, email)
        fillBlanks(R.id.password_cadastro_activity, senha)

        clickOnButton(R.id.btn_enter_cadastro_activity)

        onView(withText("Cadastro realizado com sucesso!"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser() {
        fillBlanks(R.id.user_email_login_activity, email)
        fillBlanks(R.id.password_login_activity, senha)
        clickOnButton(R.id.btn_enter_login_activity)

        onView(withText("Login efetuado"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun accountActivityShowsUsernameAndEmail() {
        mockkUserPreferences()
        val usuario = User(
            email,
            senha,
            username
        )

        val _user: MutableStateFlow<User?> = MutableStateFlow(null)
        _user.value = usuario
        val user: StateFlow<User?> = _user

        ActivityScenario.launch(MainActivity::class.java)
            .onActivity {
                it.user = user
            }

        clickOnButton(R.id.btn_account_main_activity)

        onView(withId(R.id.user_email_account_activity)).check(
            matches(withText(user.value?.email))
        )
        onView(withId(R.id.username_account_activity)).check(
            matches(withText(user.value?.name))
        )

        clickOnButton(R.id.btn_return_account_activity)
    }

    @Test
    fun searchABook() {
        mockkUserPreferences()

        ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(2000)

        fillBlanks(R.id.edit_text_main_activity, pesquisa)
        clickOnButton(R.id.btn_search_main_activity)

        Thread.sleep(2000)

        onView(withId(R.id.recycler_search_activity)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun searchABookThatDoesNotExist() {
        mockkUserPreferences()

        ActivityScenario.launch(MainActivity::class.java)

        fillBlanks(R.id.edit_text_main_activity, "lksjdfaljklasf")
        clickOnButton(R.id.btn_search_main_activity)

        Thread.sleep(400)
        onView(withText("Erro ao pesquisar"))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    @Test
    fun addBookToBooklist() {
        searchABook()

        onView(withText(pesquisa)).perform(click())

        Thread.sleep(1000)

        clickOnButton(R.id.btn_add_book_details)

        Thread.sleep(1000)

        onView(withId(R.id.btn_close_saved_book_dialog)).check(
            matches(isDisplayed())
        )
    }

    @Test
    fun goToBooklistThroughDialog() {
        clickOnButton(R.id.btn_booklist_saved_book_dialog)

        onView(withText(pesquisa)).perform(
            click()
        )
        onView(withId(R.id.book_title_saved_book_activity)).check(
            matches(withText(pesquisa))
        )
    }

    @Test
    fun removeSavedBook() {
        clickOnButton(R.id.btn_remove_saved_book_activity)

        onView(withId(R.id.btn_nao_delete_book_dialog)).check(
            matches(isDisplayed())
        )

        clickOnButton(R.id.btn_sim_delete_book_dialog)

        onView(withText(pesquisa)).check(doesNotExist())
    }

    private fun mockkUserPreferences() {
        val context = mockk<Context>()
        val preferences = UserPreferences(context)

        runBlocking {
            preferences.clearUserStatus()
            preferences.saveUserStatus(true)
        }
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