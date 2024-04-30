package com.tk203310040.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterAcitvity : AppCompatActivity() {
    private lateinit var etUser: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etUser = findViewById(R.id.etUser)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://loginregisters-8d656-default-rtdb.firebaseio.com/")
        btnRegister.setOnClickListener(View.OnClickListener {
            val username = etUser.getText().toString()
            val email = etEmail.getText().toString()
            val password = etPassword.getText().toString()
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Mohon Isi Semua data dengan benar!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                database = FirebaseDatabase.getInstance().getReference("users")
                database!!.child(username).child("username").setValue(username)
                database!!.child(username).child("email").setValue(email)
                database!!.child(username).child("password").setValue(password)
                Toast.makeText(applicationContext, "Register Berhasil!!", Toast.LENGTH_SHORT).show()
                val register = Intent(applicationContext, LoginActivity::class.java)
                startActivity(register)
            }
        })
    }
}