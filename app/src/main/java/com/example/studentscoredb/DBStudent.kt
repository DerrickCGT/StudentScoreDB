package com.example.studentscoredb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBStudent(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_NAME = "student_management"
        private val DB_VERSION = 1
        val TABLE_NAME = "student_score"
        val ID = "id"
        val SUBJECT = "subject"
        val SCORE = "score"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$SUBJECT TEXT," +
                        "$SCORE INTEGER" + ")"
                )
        db?.execSQL(query) // nullable
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // non-null assertion.  Error if null at compile time
        onCreate(db)
    }

    fun addRecord(subject: String, score: Int) {
        val values = ContentValues()

        values.put(SUBJECT, subject)
        values.put(SCORE, score)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllRecords(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deleteRecord(subject: String): Int {
        val db = this.writableDatabase

        db.beginTransaction()
        try{
            val rows = db.delete(TABLE_NAME, "subject=?", arrayOf(subject))
            db.setTransactionSuccessful()
            return rows
        } finally {
            db.endTransaction()
            db.close()
        }

    }

    fun updateRecord(subject: String, score:Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(SCORE, score)

        db.beginTransaction()
        try {
            val rows = db.update(TABLE_NAME, values, "subject=?", arrayOf(subject))
            db.setTransactionSuccessful()
            return rows
        }
        finally {
            db.endTransaction()
            db.close()
        }
    }
}