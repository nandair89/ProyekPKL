package com.tk203310040.loginregister

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CctvActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var saveButton: Button
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv)

        videoView = findViewById(R.id.videoView)
        saveButton = findViewById(R.id.saveButton)

        storageReference = FirebaseStorage.getInstance().reference.child("videos").child("cctv_video.mp4")

        storageReference.downloadUrl.addOnSuccessListener { uri ->
            videoView.setVideoURI(uri)
            videoView.start()
        }

        saveButton.setOnClickListener { view ->
            storageReference.getFile(Uri.parse("/videos/frame/file.mp4")).addOnSuccessListener { taskSnapshot ->
                Toast.makeText(this, "Video saved to device storage", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Error saving video to device storage", Toast.LENGTH_SHORT).show()
            }
        }
    }
}