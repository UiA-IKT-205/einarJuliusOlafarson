package com.example.todolistproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolistproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this right here defines what view we are looking at, nothing else
        // keep this in mind for the future
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}