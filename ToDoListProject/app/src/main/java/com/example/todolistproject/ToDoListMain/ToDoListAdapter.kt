package com.example.todolistproject.ToDoListMain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistproject.databinding.TodolistlayoutBinding
import com.example.todolistproject.databinding.TodolistmainlayoutBinding

public class ToDoListAdapter(private val ToDoList:MutableList<TodoList>): RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    class ViewHolder(val binding: TodolistmainlayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(todolist: TodoList){
            binding.ItemDescription.text = todolist.title
        }

    }
    override fun getItemCount(): Int = ToDoList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val todolist = ToDoList[position]
        holder.bind(todolist)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(TodolistmainlayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

}