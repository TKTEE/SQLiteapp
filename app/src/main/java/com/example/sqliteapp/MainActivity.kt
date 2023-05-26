package com.example.sqliteapp

import android.app.AlertDialog
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail: EditText
    lateinit var edtNum: EditText
    lateinit var btnSave: Button
    lateinit var btnView: Button
    lateinit var btnDelete: Button
    lateinit var db: SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName = findViewById(R.id.mEdtname)
        edtEmail = findViewById(R.id.mEdtemail)
        edtNum = findViewById(R.id.mEdtidnum)
        btnSave = findViewById(R.id.mBtnsave)
        btnView = findViewById(R.id.mBtnviews)
        btnDelete = findViewById(R.id.mBtndelete)
        db = openOrCreateDatabase("emobilisdb", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, email VARCHAR, id_number VARCHAR)")


        btnSave.setOnClickListener {
            var name = edtName.text.toString()
            var email = edtEmail.text.toString()
            var id_number = edtNum.text.toString()
            if (name.isEmpty()) {
                edtName.setError("Please fill this input")
                edtName.requestFocus()
            } else if (email.isEmpty()) {
                edtEmail.setError("Please fill this input")
                edtEmail.requestFocus()
            } else if (email.isEmpty()) {
                edtNum.setError("Please fill this input")
                edtNum.requestFocus()
            } else{
                db.execSQL("INSERT INTO users VALUES($name, $email, $id_number)")
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                edtName.setText(null)
                edtEmail.setText(null)
                edtNum.setText(null)
            }

        }
        btnView.setOnClickListener {
            var cursor = db.rawQuery("SELECT * FROM users", null)
            if(cursor.count == 0){
                displayUsers("NO RECORDS", "Sorry, no data")
            }else{
                var buffer = StringBuffer()
                while(cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedNum = cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedNum+"\n")
                }
                displayUsers("USERS",buffer.toString())
            }
        }
        btnDelete.setOnClickListener {
        var number = edtNum.text.toString()
            if (number.isEmpty()){
                edtNum.setError("Please fill this input")
                edtNum.requestFocus()
            }else{
                var cursor = db.rawQuery("SELECT * FROM users WHERE id_number=$number", null)
                if(cursor.count==0){
                    displayUsers("NO USER","Sorry, no data")
                }else{
                    db.execSQL("DELETE FROM users WHERE id_number=$number")
                    displayUsers("SUCCESS","User deleted!")
                    edtNum.setText(null)
                }
            }
}

    }
    fun displayUsers(title:String, message:String){
        var alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Close", null)
        alertDialog.create().show()
    }

}