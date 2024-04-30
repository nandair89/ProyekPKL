package com.tk203310040.loginregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PintuActivity : AppCompatActivity() {
    private lateinit var pinEditText: EditText
    private lateinit var unlockButton: Button
    private lateinit var lockButton: Button
    private val database = FirebaseDatabase.getInstance()
    private val pinRef = database.getReference("pin")
    private val doorStatusRef = database.getReference("doorStatus")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pintu)
        pinEditText = findViewById(R.id.pinEditText)
        unlockButton = findViewById(R.id.unlockButton)
        lockButton = findViewById(R.id.lockButton)

        unlockButton.setOnClickListener {
            val pin = pinEditText.text.toString()
            checkPin(pin, true)
        }

        lockButton.setOnClickListener {
            val pin = pinEditText.text.toString()
            checkPin(pin, false)
        }
    }

    private fun checkPin(pin: String, isUnlock: Boolean) {
        pinRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val storedPin = dataSnapshot.getValue(String::class.java)
                if (storedPin!= null && storedPin == pin) {
                    // PIN is correct, update door status
                    if (isUnlock) {
                        doorStatusRef.setValue("open")
                    } else {
                        doorStatusRef.setValue("close")
                    }
                } else {
                    // PIN is incorrect, show error message
                    Toast.makeText(this@PintuActivity, "PIN salah", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}