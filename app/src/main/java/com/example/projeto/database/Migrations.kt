package com.example.projeto.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val Migration_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                CREATE TABLE IF NOT EXISTS BookList (
                `idList` TEXT PRIMARY KEY NOT NULL,
                `emailUser` TEXT NOT NULL,
                `list` TEXT)
            """
        )
    }
}

val Migration_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """ALTER TABLE User ADD name TEXT"""
        )
    }
}

val Migration_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE Book ADD description TEXT"
        )
    }
}

val Migration_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE Book")
    }
}

val Migration_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS SavedBook (
            `image` TEXT,
            `author` TEXT,
            `idBook` TEXT NOT NULL,
            `description` TEXT,
            `userEmail` TEXT NOT NULL,
            `id` TEXT PRIMARY KEY NOT NULL,
            `title` TEXT NOT NULL
            )
        """)
    }
}