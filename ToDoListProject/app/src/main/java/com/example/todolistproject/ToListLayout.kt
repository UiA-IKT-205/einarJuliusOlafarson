package com.example.todolistproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolistproject.TodoListItem.ToDoItem
import com.example.todolistproject.ToDoListMain.TodoList
import com.example.todolistproject.databinding.FragmentToListLayoutBinding
import com.example.todolistproject.TodoListItem.ToDoItemAdapter


class ToListLayout : Fragment() {

    private var _binding: FragmentToListLayoutBinding ?= null
    private val binding get() = _binding!!
    private val todoitemtest: MutableList<ToDoItem> = mutableListOf(ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),
        ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),
        ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),
        ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false),ToDoItem("Take out trash", false), ToDoItem("Finish homework", false))
    private val todolistest: MutableList<TodoList> = mutableListOf(TodoList("Test", todoitemtest))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentToListLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        binding.todolistrecycler.layoutManager = LinearLayoutManager(context)
        binding.todolistrecycler.adapter = ToDoItemAdapter(todoitemtest)

        binding.addList.setOnClickListener {
            Toast.makeText(context, "Button has been clicked", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ToListLayout().apply {
                arguments = Bundle().apply {
                }
            }
    }
}