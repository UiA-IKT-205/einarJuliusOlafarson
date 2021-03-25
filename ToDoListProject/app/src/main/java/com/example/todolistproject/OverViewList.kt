package com.example.todolistproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.databinding.ActivityMainBinding
import com.example.todolistproject.databinding.FragmentOverViewListBinding


// Will be updated to be an activity for my lists so they can be edited in detail

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private var binding: FragmentOverViewListBinding? = null

class OverViewList : AppCompatActivity() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOverViewListBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    }

}