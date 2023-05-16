package com.example.studentscoredb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddRecord = findViewById<Button>(R.id.btnAddRecord)
        btnAddRecord.setOnClickListener {
            val db = DBStudent(this, null)

            try{
                val etSubject = findViewById<EditText>(R.id.etSubject)
                val etScore = findViewById<EditText>(R.id.etScore)
                val subject = etSubject.text.toString()
                val score = etScore.text.toString().toInt()

                db.addRecord(subject, score)

                // Toast to message on the screen
                Toast.makeText(this, "$subject added to database", Toast.LENGTH_SHORT).show()

                etSubject.text.clear()
                etScore.text.clear()
            }
            catch (e: java.lang.Exception){
                Toast.makeText(this, "Invalid Input! $e", Toast.LENGTH_SHORT).show()
            }
        }

        val btnPrintRecords = findViewById<Button>(R.id.btnPrintRecord)
        btnPrintRecords.setOnClickListener {
            val db = DBStudent(this,null)
            val cursor = db.getAllRecords()

            cursor!!.moveToFirst()
            val tvRecord = findViewById<TextView>(R.id.tvRecord)
            tvRecord.text = "-All Subjects and Scores-\n"

            if (cursor.moveToFirst()){
                do{
                    tvRecord.append(cursor.getString(cursor.getColumnIndexOrThrow(DBStudent.ID)) + ". " +
                            cursor.getString(cursor.getColumnIndexOrThrow(DBStudent.SUBJECT)) + ": " +
                            cursor.getString(cursor.getColumnIndexOrThrow(DBStudent.SCORE)) + "\n")
                }
               while (cursor.moveToNext())
            }
            db.close()
        }

        val btnDeleteRecord = findViewById<Button>(R.id.btnDeleteRecord)
        btnDeleteRecord.setOnClickListener {
            val db = DBStudent(this, null)
            val subjectName = findViewById<EditText>(R.id.etSubject).text.toString()
            val rows = db.deleteRecord(subjectName)

            Toast.makeText(
                this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 user deleted"
                    else -> "$rows users deleted"
                },
                Toast.LENGTH_LONG
            ).show()
        }

        val btnUpdateRecord = findViewById<Button>(R.id.btnUpdateRecord)
        btnUpdateRecord.setOnClickListener {
            val db = DBStudent(this, null)
            try{
                val subjectName = findViewById<EditText>(R.id.etSubject).text.toString()
                val score = findViewById<EditText>(R.id.etScore).text.toString().toInt()
                val rows = db.updateRecord(subjectName, score)
                Toast.makeText(this, "$rows users updated", Toast.LENGTH_LONG).show()
            }
            catch (e: java.lang.Exception){
                Toast.makeText(this, "Invalid Input! $e", Toast.LENGTH_LONG).show()
            }
        }
    }
}