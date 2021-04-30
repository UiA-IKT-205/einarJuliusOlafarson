package com.example.todolistproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todolistproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val TAG:String = "MyTodo:MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        auth.signInAnonymously()
    }

    private fun signInAnonymously(){
        val x = auth.signInAnonymously().addOnSuccessListener {
            Log.d(TAG, "Login successful ${it.user.toString()}")
        }.addOnFailureListener{
            Log.e(TAG, "Login failed", it)
        }
    }

}