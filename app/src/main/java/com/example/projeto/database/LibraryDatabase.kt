package com.example.projeto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.projeto.database.dao.SavedBookDao
import com.example.projeto.database.dao.UserDao
import com.example.projeto.migrations.Migration_1_2
import com.example.projeto.migrations.Migration_2_3
import com.example.projeto.migrations.Migration_3_4
import com.example.projeto.migrations.Migration_4_5
import com.example.projeto.migrations.Migration_5_6
import com.example.projeto.model.SavedBook
import com.example.projeto.model.User

@Database(entities = [User::class, SavedBook::class], version = 6)

abstract class LibraryDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun savedBookDao(): SavedBookDao

    companion object {
        @Volatile
        private var db: LibraryDatabase? = null

        fun instance(context: Context): LibraryDatabase {
            return db?: Room.databaseBuilder(
                context,
                LibraryDatabase::class.java,
                "BookScape.db"
            ).addMigrations(Migration_1_2, Migration_2_3, Migration_3_4, Migration_4_5, Migration_5_6)
                .build()
        }
    }

}