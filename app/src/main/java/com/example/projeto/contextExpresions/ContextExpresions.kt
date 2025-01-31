package com.example.projeto.contextExpresions

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.goTo(clazz: Class<*>, intent: Intent.() -> Unit = {}) {
     Intent(this, clazz).apply{
        intent()
        startActivity(this)
    }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}