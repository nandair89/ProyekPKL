package com.tk203310040.loginregister

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var etUser: EditText
    private lateinit var etPassword: EditText
    private lateinit var textRegister: TextView
    private lateinit var btnLogin: Button
    private lateinit var database: DatabaseReference
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUser = findViewById(R.id.etUser)
        etPassword = findViewById(R.id.etPassword)
        textRegister = findViewById(R.id.textRegister)
        btnLogin = findViewById(R.id.btnLogin)
        textRegister.setOnClickListener(View.OnClickListener {
            val register = Intent(applicationContext, RegisterAcitvity::class.java)
            startActivity(register)
        })
        btnLogin.setOnClickListener(View.OnClickListener {
            val username = etUser.getText().toString()
            val password = etPassword.getText().toString()
            database = FirebaseDatabase.getInstance().getReference("users")
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Username atau Password Belum diisi!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                database!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(username).exists()) {
                            if (snapshot.child(username).child("password").getValue(String::class.java) == password)
                            {
                                Toast.makeText(applicationContext, "Login Berhasil!!",Toast.LENGTH_SHORT).show()
                                val masuk = Intent(applicationContext, MainActivity::class.java)
                                startActivity(masuk)
                            }
                        } else {
                            Toast.makeText(applicationContext,"Data belum terdaftar!!",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        })
    }
}