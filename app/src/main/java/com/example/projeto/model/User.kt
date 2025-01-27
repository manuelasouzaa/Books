package com.example.projeto.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.StateFlow

@Entity
data class User (

    @PrimaryKey
    val email: String,
    val password: String,
    val name: String

)