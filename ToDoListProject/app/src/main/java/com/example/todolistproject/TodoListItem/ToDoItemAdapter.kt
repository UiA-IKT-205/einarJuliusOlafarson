package com.example.todolistproject.TodoListItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistproject.databinding.TodolistlayoutBinding

public class ToDoItemAdapter(private val ToDoItem:MutableList<ToDoItem>): RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: TodolistlayoutBinding): RecyclerView.ViewHolder(binding.root){


        fun bind(todolist: ToDoItem){
            binding.ItemDescription.text = todolist.description
            binding.IsFinished.text = todolist.isDone.toString()
        }

    }
    override fun getItemCount(): Int = ToDoItem.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val todolist = ToDoItem[position]
        holder.bind(todolist)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(TodolistlayoutBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

}