package com.tk203310040.loginregister

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var btnOk: Button
    private lateinit var btnCctv: Button
    private lateinit var txtSuhu: TextView
    private lateinit var txtUdara: TextView
    private lateinit var txtStatus: TextView
    private lateinit var databaseRef: DatabaseReference
    private lateinit var notificationHelper: NotifikasiClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnOk = findViewById(R.id.btnOk)
        btnCctv = findViewById(R.id.btnCctv)
        txtSuhu = findViewById(R.id.txtSuhu)
        txtUdara = findViewById(R.id.txtUdara)
        txtStatus = findViewById(R.id.txtStatus)
        notificationHelper = NotifikasiClass(this)

        databaseRef = FirebaseDatabase.getInstance().reference.child("Sensor")
        databaseRef.addValueEventListener(object : ValueEventListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                val sensor1Value = snapshot.child("Sensor1").value.toString()
                val sensor2Value = snapshot.child("Sensor2").value.toString()
                val sensor3Value = snapshot.child("Sensor3").value.toString()

                txtSuhu.text = sensor2Value
                txtUdara.text = sensor1Value
                // Tampilkan nilai sensor lainnya jika diperlukan

                val sensor1IntValue = sensor1Value.toIntOrNull()
                if (sensor1IntValue!= null){
                    if (sensor1IntValue >= 100) {
                        txtUdara.text = "Status: Tercemar"
                    } else {
                        txtUdara.text = "Status: Aman"
                    }
                } else {
                    txtStatus.text = "Status: Invalid Sensor Value"
                }
                val sensor3StringValue = sensor3Value.toString()
                if (sensor3StringValue!=null){
                    if (sensor3StringValue == "Asap Terdeteksi"){
                        txtStatus.text = "Asap Terdeksi"
                        notificationHelper.showNotification("Alert", "Asap Terdeteksi")
                    } else {
                        txtStatus.text = "Asap Tidak Terdeteksi"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        btnOk.setOnClickListener(View.OnClickListener {
            val pintu = Intent(applicationContext, PintuActivity::class.java)
            startActivity(pintu)
        })
        btnCctv.setOnClickListener(View.OnClickListener {
            val cctv = Intent(applicationContext, CctvActivity::class.java)
            startActivity(cctv)
        })
    }
}